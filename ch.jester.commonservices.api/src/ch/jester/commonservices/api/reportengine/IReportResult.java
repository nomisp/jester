package ch.jester.commonservices.api.reportengine;

import java.io.File;
import java.io.OutputStream;

import ch.jester.commonservices.exceptions.ProcessingException;

/**
 * Das Resultat einer Reporterzeugung {@link IReportEngine#generate(IReport, java.util.Collection)}
 *
 */
public interface IReportResult {
	/**
	 * Export als...
	 *
	 */
	enum ExportType{
		PDF("pdf", "Pdf"), HTML("html","Html"), XML("xml","Xml"), EXCEL("xls","Excel"), CSV("csv","Csv");
		
		ExportType(String pExtension, String pName){
			extension = pExtension;
			name = pName;
		}
		public String getExtension(){
			return extension;
		}
		public String getName(){
			return name;
		}
		
		private String extension;
		private String name;
	}
	/**
	 * Soll vor {@link IReportResult#export(ExportType)} aufgerufen werden.
	 * @param ex
	 * @return true (Export möglich) / false (Export nicht möglich)
	 */
	public boolean canExport(ExportType ex);
	/**
	 * Exportiert den erzeugten Report in das übergebene Format.
	 * @param ex der ExportType
	 * @return das File wohin der Export erfolgt ist.
	 * @throws ProcessingException
	 */
	public File export(ExportType ex) throws ProcessingException;
	
	public void export(ExportType ex, OutputStream pOutputStream) throws ProcessingException;
}
