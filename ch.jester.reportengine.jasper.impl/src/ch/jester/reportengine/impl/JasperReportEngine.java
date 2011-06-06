package ch.jester.reportengine.impl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.HashMap;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import org.osgi.service.component.ComponentContext;

import ch.jester.common.reportengine.DefaultReportFactory;
import ch.jester.common.reportengine.DefaultReportResult;
import ch.jester.commonservices.api.components.IComponentService;
import ch.jester.commonservices.api.io.IFileManager;
import ch.jester.commonservices.api.logging.ILogger;
import ch.jester.commonservices.api.logging.ILoggerFactory;
import ch.jester.commonservices.api.reportengine.IReport;
import ch.jester.commonservices.api.reportengine.IReportEngine;
import ch.jester.commonservices.api.reportengine.IReportEngineFactory;
import ch.jester.commonservices.api.reportengine.IReportResult;
import ch.jester.commonservices.exceptions.ProcessingException;

public class JasperReportEngine implements IReportEngine, IComponentService<Object>{
	private ILogger mLogger;
	private IFileManager mTempFileManager;
    private IReportEngineFactory factory = new DefaultReportFactory();
    public JasperReportEngine(){
    	factory.createReport("ch.jester.reportengine.jasper.impl", "playerlist", "Player List", "reports", "reports/PlayerList.jrxml");
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
			//JasperExportManager.exportReportToPdfFile(jasperPrint,"PlayerList.pdf");
			return new JasperReportResult(jasperPrint);
		} catch (JRException e) {
			throw new ProcessingException(e);
		} catch (IOException e) {
			throw new ProcessingException(e);
		}
	}

	@Override
	public IReportEngineFactory getFactory() {
		return factory;
	}
	
	class JasperReportResult extends DefaultReportResult<JasperPrint>{
		public JasperReportResult(JasperPrint pResult) {
			super(pResult);
		}

		@Override
		public File export(ExportType ex) throws ProcessingException {
			try{
				switch(ex){
				case HTML:
					File file = mTempFileManager.createTempFileWithExtension("html");					
					JasperExportManager.exportReportToHtmlFile(mResult,file.getAbsolutePath());
					return file;
				case PDF:
					//JasperExportManager.exportReportToPdfFile(mResult,mOutptuName+".pdf");
					File f = mTempFileManager.createTempFileWithExtension("pdf");
					JasperExportManager.exportReportToPdfFile(mResult, f.getAbsolutePath());
					return f;
				case XML:
					File f1 = mTempFileManager.createTempFileWithExtension("xml");
					JasperExportManager.exportReportToXmlFile(mResult,f1.getAbsolutePath(),true);
					return f1;
				}
				
			}catch(JRException e){
				throw new ProcessingException(e);
			}
			return null;
			
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
