package ch.jester.reportengine.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.HashMap;

import org.osgi.service.component.ComponentContext;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import ch.jester.common.reportengine.DefaultReportFactory;
import ch.jester.common.reportengine.DefaultReportResult;
import ch.jester.commonservices.api.components.IComponentService;
import ch.jester.commonservices.api.logging.ILogger;
import ch.jester.commonservices.api.logging.ILoggerFactory;
import ch.jester.commonservices.api.reportengine.IReport;
import ch.jester.commonservices.api.reportengine.IReportEngine;
import ch.jester.commonservices.api.reportengine.IReportEngineFactory;
import ch.jester.commonservices.api.reportengine.IReportResult;
import ch.jester.commonservices.exceptions.ProcessingException;

public class JasperReportEngine implements IReportEngine, IComponentService<ILoggerFactory>{
	private ILogger mLogger;
    private IReportEngineFactory factory = new DefaultReportFactory();
    public JasperReportEngine(){
    	factory.createReport("ch.jester.reportengine.jasper.impl", "playerlist", "Player List", "PlayerList.jrxml");
    }
    
	@Override
	public IReportResult generate(IReport pReport, Collection<?> pBean) throws ProcessingException{
		try {
		    HashMap<String, Object> parameter = new HashMap<String, Object>();
			JRBeanCollectionDataSource beancollection = new JRBeanCollectionDataSource(pBean);
			InputStream stream = pReport.getFileAsStream();
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
		public void export(ExportType ex) throws ProcessingException {
			if(mOutptuName==null){
				throw new ProcessingException("Name mustn't be empty");
			}
			
			try{
				switch(ex){
				case HTML:
					JasperExportManager.exportReportToHtmlFile(mResult,mOutptuName+".html");
					break;
				case PDF:
					JasperExportManager.exportReportToPdfFile(mResult,mOutptuName+".pdf");
					break;
				case XML:
					JasperExportManager.exportReportToXmlFile(mResult,mOutptuName+".xml",true);
					break;
				}
				
			}catch(JRException e){
				throw new ProcessingException(e);
			}
			
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
	public void bind(ILoggerFactory pT) {
		mLogger = pT.getLogger(getClass());
		mLogger.info("ReportEnginge started");
		
	}

	@Override
	public void unbind(ILoggerFactory pT) {
		// TODO Auto-generated method stub
		
	}
}