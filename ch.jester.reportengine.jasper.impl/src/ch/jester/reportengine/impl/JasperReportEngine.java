package ch.jester.reportengine.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collection;
import java.util.HashMap;

import javax.servlet.http.HttpSession;

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

import org.eclipse.core.runtime.Platform;
import org.osgi.service.component.ComponentContext;

import ch.jester.common.reportengine.DefaultReportRepository;
import ch.jester.common.reportengine.DefaultReportResult;
import ch.jester.common.utility.AdapterBinding;
import ch.jester.commonservices.api.components.IComponentService;
import ch.jester.commonservices.api.io.IFileManager;
import ch.jester.commonservices.api.logging.ILogger;
import ch.jester.commonservices.api.logging.ILoggerFactory;
import ch.jester.commonservices.api.reportengine.IReport;
import ch.jester.commonservices.api.reportengine.IReportEngine;
import ch.jester.commonservices.api.reportengine.IReportRepository;
import ch.jester.commonservices.api.reportengine.IReportResult;
import ch.jester.commonservices.api.web.IHTTPSessionAware;
import ch.jester.commonservices.exceptions.ProcessingException;
import ch.jester.commonservices.util.ServiceUtility;
import ch.jester.reportengine.impl.internal.Initializer;

public class JasperReportEngine implements IReportEngine, IComponentService<Object>{
	private ILogger mLogger;
	private IFileManager mTempFileManager;
    private IReportRepository factory = new DefaultReportRepository();
    public JasperReportEngine(){
    	factory.createBundleReport("ch.jester.reportengine.jasper.impl", "playerlist", "Player List", "reports", "reports/PlayerList.jrxml");
    	new Initializer().load(factory);
    }
    
	@Override
	public IReportResult generate(IReport pReport, Collection<?> pBean) throws ProcessingException{
		try {
		    HashMap<String, Object> parameter = new HashMap<String, Object>();
			JRBeanCollectionDataSource beancollection = new JRBeanCollectionDataSource(pBean);
			InputStream stream = pReport.getInstalledFileAsStream();
			JasperReport jasperReport =JasperCompileManager.compileReport(stream);
			stream.close();
			JasperPrint jasperPrint =JasperFillManager.fillReport(jasperReport, parameter, beancollection);
			return new JasperReportResult(jasperPrint, this);
		} catch (JRException e) {
			throw new ProcessingException(e);
		} catch (IOException e) {
			throw new ProcessingException(e);
		}
	}

	@Override
	public IReportRepository getRepository() {
		return factory;
	}
	
	class JasperReportResult extends DefaultReportResult<JasperPrint>{
		ServiceUtility su = new ServiceUtility();
		HttpSessionAdapter sessionadapter = new HttpSessionAdapter();
		class HttpSessionAdapter implements IHTTPSessionAware{
			HttpSession session;
			@Override
			public HttpSession getSession() {
				return session;
			}

			@Override
			public void setSession(HttpSession pSession) {
				session = pSession;
			}
			
		}
		
		public JasperReportResult(JasperPrint pResult, IReportEngine pEngine) {
			super(pResult, pEngine);
			AdapterBinding binding = new AdapterBinding(this);
			binding.add(sessionadapter, IHTTPSessionAware.class);
			binding.bind();
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
				switch(ex){
				case HTML:
					exporter = new JRHtmlExporter();
					exporter.setParameter(JRHtmlExporterParameter.JASPER_PRINT, mResult);
					exporter.setParameter(JRHtmlExporterParameter.OUTPUT_STREAM, output);
					exporter.setParameter(JRHtmlExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);
					exporter.setParameter(JRHtmlExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
					exporter.setParameter(JRHtmlExporterParameter.IS_OUTPUT_IMAGES_TO_DIR, Boolean.FALSE);
					exporter.setParameter(JRHtmlExporterParameter.IGNORE_PAGE_MARGINS, Boolean.TRUE);
					exporter.setParameter(JRHtmlExporterParameter.FLUSH_OUTPUT, Boolean.TRUE);
					exporter.setParameter(JRHtmlExporterParameter.FRAMES_AS_NESTED_TABLES, Boolean.TRUE);
					exporter.setParameter(JRHtmlExporterParameter.IMAGES_URI,"image?image=");
					exporter.setParameter(JRHtmlExporterParameter.IMAGES_DIR,su.getService(IFileManager.class).getRootTempDirectory());
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
				if(sessionadapter.getSession()==null){
					output.close();
				}
		
				
				
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
			return Platform.getAdapterManager().getAdapter(this, adapter);
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
			mLogger.info("ReportEnginge started");
		}
		if(pT instanceof IFileManager){
			mTempFileManager=(IFileManager) pT;
		}

		
	}

	@Override
	public void unbind(Object pT) {
		// TODO Auto-generated method stub
		
	}
}
