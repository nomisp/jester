package ch.jester.reportengine.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collection;
import java.util.HashMap;

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
		public boolean canExport(ExportType ex) {
			return true;
		}

		@Override
		public File export(ExportType ex) throws ProcessingException {
			File file = null;
			switch(ex){
			case HTML:
				file = mTempFileManager.createTempFileWithExtension("html");	
				break;
			case PDF:
				file = mTempFileManager.createTempFileWithExtension("pdf");
				break;
			case XML:
				file = mTempFileManager.createTempFileWithExtension("xml");
				break;
			
			case EXCEL:
				file = mTempFileManager.createTempFileWithExtension("xls");
				break;
			case CSV:
				file = mTempFileManager.createTempFileWithExtension("csv");
				break;
			}
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
				switch(ex){
				case HTML:
					exporter = new JRHtmlExporter();
					//JasperExportManager.exportReportToHtmlFile(mResult,file.getAbsolutePath());
				//	return f;
					exporter.setParameter(JRHtmlExporterParameter.JASPER_PRINT, mResult);
					exporter.setParameter(JRHtmlExporterParameter.OUTPUT_STREAM, output);
			//		exporter.setParameter(JRHtmlExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);
			//		exporter.setParameter(JRHtmlExporterParameter.IS_AUTO_DETECT_CELL_TYPE, Boolean.TRUE);
					exporter.setParameter(JRHtmlExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);
					exporter.setParameter(JRHtmlExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
					break;
				case PDF:
					exporter = new JRPdfExporter();
					//JasperExportManager.exportReportToPdfFile(mResult,mOutptuName+".pdf");
					//f = mTempFileManager.createTempFileWithExtension("pdf");
					//JasperExportManager.exportReportToPdfFile(mResult, f.getAbsolutePath());
				//	return f;
					exporter.setParameter(JRPdfExporterParameter.JASPER_PRINT, mResult);
					exporter.setParameter(JRPdfExporterParameter.OUTPUT_STREAM, output);
				//	exporter.setParameter(JRPdfExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);
					//exporter.setParameter(JRPdfExporterParameter.IS_AUTO_DETECT_CELL_TYPE, Boolean.TRUE);
				//	exporter.setParameter(JRPdfExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);
					//exporter.setParameter(JRPdfExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
					break;
				case XML:
					exporter = new JRXmlExporter();
					//File f1 = mTempFileManager.createTempFileWithExtension("xml");
					//JasperExportManager.exportReportToXmlFile(mResult,f1.getAbsolutePath(),true);
					//return f;
					exporter.setParameter(JRXmlExporterParameter.JASPER_PRINT, mResult);
					exporter.setParameter(JRXmlExporterParameter.OUTPUT_STREAM, output);
					//exporter.setParameter(JRXmlExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);
					//exporter.setParameter(JRXmlExporterParameter.IS_AUTO_DETECT_CELL_TYPE, Boolean.TRUE);
					//exporter.setParameter(JRXmlExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);
					//exporter.setParameter(JRXmlExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
					break;
				
				case EXCEL:
					exporter = new JRXlsExporter();
					exporter.setParameter(JRXlsExporterParameter.JASPER_PRINT, mResult);
					exporter.setParameter(JRXlsExporterParameter.OUTPUT_STREAM, output);
					exporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);
					exporter.setParameter(JRXlsExporterParameter.IS_AUTO_DETECT_CELL_TYPE, Boolean.TRUE);
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
