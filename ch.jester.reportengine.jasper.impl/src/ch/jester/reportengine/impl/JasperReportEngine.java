package ch.jester.reportengine.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

import javax.servlet.http.HttpSession;

import messages.Messages;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRCsvExporter;
import net.sf.jasperreports.engine.export.JRCsvExporterParameter;
import net.sf.jasperreports.engine.export.JRHtmlExporter;
import net.sf.jasperreports.engine.export.JRHtmlExporterParameter;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRPdfExporterParameter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;
import net.sf.jasperreports.engine.export.JRXmlExporter;
import net.sf.jasperreports.engine.export.JRXmlExporterParameter;
import net.sf.jasperreports.j2ee.servlets.ImageServlet;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.osgi.service.component.ComponentContext;

import ch.jester.common.reportengine.DefaultReportRepository;
import ch.jester.common.reportengine.DefaultReportResult;
import ch.jester.common.utility.AdapterBinding;
import ch.jester.common.utility.StopWatch;
import ch.jester.commonservices.api.components.IComponentService;
import ch.jester.commonservices.api.io.IFileManager;
import ch.jester.commonservices.api.logging.ILogger;
import ch.jester.commonservices.api.logging.ILoggerFactory;
import ch.jester.commonservices.api.reportengine.IBundleReport;
import ch.jester.commonservices.api.reportengine.IReport;
import ch.jester.commonservices.api.reportengine.IReportEngine;
import ch.jester.commonservices.api.reportengine.IReportRepository;
import ch.jester.commonservices.api.reportengine.IReportResult;
import ch.jester.commonservices.api.web.IHTTPSessionAware;
import ch.jester.commonservices.exceptions.ProcessingException;
import ch.jester.commonservices.util.ServiceUtility;
import ch.jester.model.Category;
import ch.jester.model.Player;
import ch.jester.model.Tournament;
import ch.jester.reportengine.impl.internal.Initializer;




/**
 * 
 */
public class JasperReportEngine implements IReportEngine, IComponentService<Object>{

	private static String COMPILE_JOB_TITLE =Messages.JasperReportEngine_compile_job_title;
	
	private ReentrantLock compilingLock = new ReentrantLock();
	
	private CompileCache cache = new CompileCache();
	
	private static String BUNDLE_REPORT_LOCATION="reports"; //$NON-NLS-1$
	
	private static String BUNDLE_ID ="ch.jester.reportengine.jasper.impl";
	
	private ILogger mLogger;
	private IFileManager mTempFileManager;

    private IReportRepository factory = new DefaultReportRepository();

    public JasperReportEngine(){
    	IBundleReport report = factory.createBundleReport(BUNDLE_ID, "playerlist", Messages.JasperReportEngine_player_report_name, BUNDLE_REPORT_LOCATION, "reports/PlayerList.jrxml"); //$NON-NLS-1$ //$NON-NLS-3$
    	report.setInputBeanClass(Player.class);
    	report = factory.createBundleReport(BUNDLE_ID, "pairinglist_tournament", Messages.JasperReportEngine_tournament_report_name, BUNDLE_REPORT_LOCATION, "reports/PairingList.jrxml"); //$NON-NLS-1$ //$NON-NLS-3$
    	report.setInputBeanClass(Tournament.class);
    	report = factory.createBundleReport(BUNDLE_ID, "pairinglist_category", Messages.JasperReportEngine_category_pairing_name, BUNDLE_REPORT_LOCATION, "reports/PairingListCat.jrxml"); //$NON-NLS-1$ //$NON-NLS-3$
    	report.setInputBeanClass(Category.class);
    	IBundleReport src = factory.createBundleReport(BUNDLE_ID, null, null, BUNDLE_REPORT_LOCATION, "reports/Category_RoundSubReport.jrxml"); //$NON-NLS-1$
    	cache.addCachedReport(src, null, null);
    	src = factory.createBundleReport(BUNDLE_ID, null, null, BUNDLE_REPORT_LOCATION, "reports/Category_sub.jrxml"); //$NON-NLS-1$
    	cache.addCachedReport(src, null, null);
    	new Initializer().load(factory);
    	
    	
    }
    

    
    protected void precompileAllReports(IProgressMonitor monitor) throws ProcessingException{
    	compilingLock.lock();
		int visibleReports = JasperReportEngine.this.getRepository().getReports().size();
		int sourceReports = cache.keySet().size();
		int work = visibleReports+sourceReports;
		monitor.beginTask(Messages.JasperReportEngine_precompile_task, work);
		
		
    	List<IReport> reports = getRepository().getReports();
    	Iterator<IReport> sourceIterator = cache.keySet().iterator();
    	while(sourceIterator.hasNext()){
    		IReport report = sourceIterator.next();
    		cache.compileReport(report);
    	}
    	for(IReport report:reports){
    		monitor.subTask(Messages.JasperReportEngine_compile_task+report.getVisibleName());
    		cache.compileReport(report);
			monitor.worked(1);
			
    	}
    	compilingLock.unlock();
    }
    
    private void checkInput(IReport pReport, Collection<?> pBean){
    	if(pBean.isEmpty()){
    		throw new ProcessingException(Messages.JasperReportEngine_ex_no_input);
    	}
    	if(pReport.getInputBeanClass()==null){
    		throw new ProcessingException(Messages.JasperReportEngine_ex_no_input_class+pReport.getVisibleName()+Messages.JasperReportEngine_ex_definied);
    	}
    	Class<?> inputClass = pBean.iterator().next().getClass();
    	if(!pReport.getInputBeanClass().isAssignableFrom(inputClass)){
    		throw new ProcessingException(Messages.JasperReportEngine_ex_class_not_handle);
    	}
    	
    }
    
    public void startUserNotifcationJob(){
    	Job job = new Job(Messages.JasperReportEngine_waiting_for+COMPILE_JOB_TITLE+"'"){ //$NON-NLS-2$
			
			@Override
			protected IStatus run(IProgressMonitor monitor) {
				monitor.beginTask(Messages.JasperReportEngine_waiting, IProgressMonitor.UNKNOWN);
				compilingLock.lock();
				compilingLock.unlock();
				monitor.done();
				return Status.OK_STATUS;
			}
			
		};
		job.setUser(true);
		job.schedule();
    }
    
	@Override
	public IReportResult generate(IReport pReport, Collection<?> pBean) throws ProcessingException{
		try {
			boolean gotLock = compilingLock.tryLock();
			if(!gotLock){
				startUserNotifcationJob();
			}
			
			
			compilingLock.lock();
			checkInput(pReport, pBean);
			mLogger.info(Messages.JasperReportEngine_gen_input_size+pBean.size()+Messages.JasperReportEngine_objects);
			StopWatch watch = new StopWatch();
			watch.start();
		    HashMap<String, Object> parameter = new HashMap<String, Object>();
		    File reportDir = mTempFileManager.getFolderInWorkingDirectory(IReportEngine.TEMPLATE_DIRECTROY+"/reports/"); //$NON-NLS-1$
		    parameter.put("SUBREPORT_DIR", reportDir.getAbsolutePath()+"/"); //$NON-NLS-1$ //$NON-NLS-2$
			JRBeanCollectionDataSource beancollection = new JRBeanCollectionDataSource(pBean);
			JasperReport cachedReport = cache.getCachedReport(pReport);
			JasperPrint jasperPrint =JasperFillManager.fillReport(cachedReport, parameter, beancollection);
			watch.stop();
			mLogger.info(Messages.JasperReportEngine_report_created+watch.getElapsedTime()+Messages.JasperReportEngine_second_unit);
			return new JasperReportResult(jasperPrint, this);
		} catch (JRException e) {
			throw new ProcessingException(e);
		}finally{
			compilingLock.unlock();
		}
	}

	@Override
	public IReportRepository getRepository() {
		return factory;
	}
	
	/**
	 */
	class JasperReportResult extends DefaultReportResult<JasperPrint>{
		ServiceUtility su = new ServiceUtility();
		HttpSessionAdapter sessionadapter;
		AdapterBinding binding;
		class HttpSessionAdapter implements IHTTPSessionAware{
			HttpSession session;
			@Override
			public HttpSession getSession() {
				return session;
			}

			@Override
			public void setSession(HttpSession pSession) {
				if(pSession==null){throw new IllegalArgumentException(Messages.JasperReportEngine_ex_session_not_null);};
				session = pSession;
			}
			
		}
		
		public JasperReportResult(JasperPrint pResult, IReportEngine pEngine) {
			super(pResult, pEngine);
			sessionadapter= new HttpSessionAdapter();
			binding = new AdapterBinding(this);
			binding.add(sessionadapter, IHTTPSessionAware.class);
		}
		@Override
		public boolean canExport(ExportType ex) {
			return true;
		}

		@Override
		public File export(ExportType ex) throws ProcessingException {
			File file = mTempFileManager.createTempFileWithExtension(ex.getExtension());
			try {
				OutputStream outputfile = new FileOutputStream(file);
				export(ex, outputfile);
			} catch (FileNotFoundException e) {
				throw new ProcessingException(e);
			}
			
			return file;
		}
		@Override
		public void export(ExportType ex, OutputStream pOutputStream)
				throws ProcessingException {
			try{
				JRExporter exporter = null;
				OutputStream output = pOutputStream;
				if(sessionadapter.getSession()!=null){
					sessionadapter.getSession().setAttribute(ImageServlet.DEFAULT_JASPER_PRINT_SESSION_ATTRIBUTE, mResult);
				}
				mLogger.info("Exporting Report. Type "+ex.getName()+"; Session = "+sessionadapter.getSession()); //$NON-NLS-1$ //$NON-NLS-2$
				switch(ex){
				case HTML:
					exporter = new JRHtmlExporter();
					exporter.setParameter(JRHtmlExporterParameter.JASPER_PRINT, mResult);
					exporter.setParameter(JRHtmlExporterParameter.OUTPUT_STREAM, output);
					exporter.setParameter(JRHtmlExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);
					exporter.setParameter(JRHtmlExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
					exporter.setParameter(JRHtmlExporterParameter.IS_OUTPUT_IMAGES_TO_DIR, Boolean.TRUE);
					exporter.setParameter(JRHtmlExporterParameter.IMAGES_DIR,new File(mTempFileManager.getRootTempDirectory()+"/image")); //$NON-NLS-1$
					exporter.setParameter(JRHtmlExporterParameter.IGNORE_PAGE_MARGINS, Boolean.TRUE);
					exporter.setParameter(JRHtmlExporterParameter.FLUSH_OUTPUT, Boolean.TRUE);
					exporter.setParameter(JRHtmlExporterParameter.FRAMES_AS_NESTED_TABLES, Boolean.TRUE);
					//exporter.setParameter(JRHtmlExporterParameter.IMAGES_URI,"image?image=");
					exporter.setParameter(JRHtmlExporterParameter.IMAGES_URI,"./image/"); //$NON-NLS-1$
					//exporter.setParameter(JRHtmlExporterParameter.IMAGES_DIR,su.getService(IFileManager.class).getRootTempDirectory());
					break;
				case PDF:
					exporter = new JRPdfExporter();
					exporter.setParameter(JRPdfExporterParameter.JASPER_PRINT, mResult);
					exporter.setParameter(JRPdfExporterParameter.OUTPUT_STREAM, output);
					break;
				case XML:
					exporter = new JRXmlExporter();
					exporter.setParameter(JRXmlExporterParameter.JASPER_PRINT, mResult);
					exporter.setParameter(JRXmlExporterParameter.OUTPUT_STREAM, output);
					break;
				
				case EXCEL:
					exporter = new JRXlsExporter();
					exporter.setParameter(JRXlsExporterParameter.JASPER_PRINT, mResult);
					exporter.setParameter(JRXlsExporterParameter.OUTPUT_STREAM, output);
					exporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);
					exporter.setParameter(JRXlsExporterParameter.IS_DETECT_CELL_TYPE, Boolean.TRUE);
					exporter.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);
					exporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
					break;
				case CSV:
					exporter = new JRCsvExporter();
					exporter.setParameter(JRCsvExporterParameter.JASPER_PRINT, mResult);
					exporter.setParameter(JRCsvExporterParameter.OUTPUT_STREAM, output);
					break;	
				}
				exporter.exportReport();
				output.flush();
				output.close();
		
				
				
			}catch(JRException e){
				throw new ProcessingException(e);
			} catch (FileNotFoundException e) {
				throw new ProcessingException(e);
			} catch (IOException e) {
				throw new ProcessingException(e);
			}
		}
		@SuppressWarnings("rawtypes")
		@Override
		public Object getAdapter(Class adapter) {
			return binding.getAdapter(adapter);
		}

		
	}

	
	@Override
	public void start(ComponentContext pComponentContext) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void stop(ComponentContext pComponentContext) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void bind(Object pT) {
		if(pT instanceof ILoggerFactory){
			ILoggerFactory fac = (ILoggerFactory) pT;
			mLogger = fac.getLogger(getClass());
			mLogger.info("ReportEnginge started"); //$NON-NLS-1$
		}
		if(pT instanceof IFileManager){
			mTempFileManager=(IFileManager) pT;
		}

		if(mTempFileManager!=null&&mLogger!=null){
			CompileJob cj = new CompileJob();
			cj.schedule();
		}
	}

	@Override
	public void unbind(Object pT) {
		// TODO Auto-generated method stub
		
	}
	class CompileJob extends Job{
		public CompileJob(){
			super(COMPILE_JOB_TITLE);
		}

		@Override
		protected IStatus run(IProgressMonitor monitor) {
	
			precompileAllReports(monitor);
			mLogger.info("Precompiling done!"); //$NON-NLS-1$
			return Status.OK_STATUS;
		}
	}
	
	class CompileCache extends HashMap<IReport, CompileCacheEntry>{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private boolean mEnabled;
		public void setEnabled(boolean pEnabled){
			mEnabled = pEnabled;
		}
	    protected void compileReport(IReport pReport) throws ProcessingException{
	    	try {
				InputStream stream = pReport.getInstalledFileAsStream();
				JasperReport jasperReport =JasperCompileManager.compileReport(stream);
				File reportDir = mTempFileManager.getFolderInWorkingDirectory(IReportEngine.TEMPLATE_DIRECTROY+"/reports"); //$NON-NLS-1$
				String fileName = pReport.getInstalledFile().getName().replace("jrxml", "jasper"); //$NON-NLS-1$ //$NON-NLS-2$
				File compiledFile = new File(reportDir.getAbsoluteFile()+"/"+fileName); //$NON-NLS-1$
				ObjectOutput out = new ObjectOutputStream(new FileOutputStream(compiledFile));
				out.writeObject(jasperReport);
				out.flush();
				out.close();
				stream.close();
				addCachedReport(pReport, jasperReport, compiledFile);
			} catch (Exception e) {
				throw new ProcessingException(e);
			}
	    }
		
		JasperReport getCachedReport(IReport pReport){
			if(get(pReport)==null || !mEnabled){
				compileReport(pReport);
			}
			return get(pReport).getJReport();
		}
		
		void addCachedReport(IReport pReport, JasperReport pCompiledReport, File pCompiledFile){
			put(pReport, new CompileCacheEntry(pReport, pCompiledReport, pCompiledFile));
		}
	}
	class CompileCacheEntry{
		private File mCompiledFile;
		private JasperReport mCompiledReport;
		private IReport mReport;
		
		public CompileCacheEntry(IReport pReport, JasperReport pCompiledReport, File pCompiledFile){
			
			mReport = pReport;
			mCompiledReport = pCompiledReport;
			mCompiledFile = pCompiledFile;
		}
		public JasperReport getJReport(){
			if(mCompiledReport==null){
				loadReportFromFS();
			}
			return mCompiledReport;
		}
		public IReport getReport(){
			return mReport;
		}
		private void loadReportFromFS(){
			try {
				ObjectInputStream ois = new ObjectInputStream(new FileInputStream(mCompiledFile));
				mCompiledReport = (JasperReport) ois.readObject();
				ois.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}


