package ch.jester.commonservices.api.reportengine;

import java.io.File;
import java.io.OutputStream;

import org.eclipse.core.runtime.IAdaptable;

import ch.jester.commonservices.exceptions.ProcessingException;

/**
 * Das Resultat einer Reporterzeugung {@link IReportEngine#generate(IReport, java.util.Collection)}
 *
 */
public interface IReportResult extends IAdaptable{
	/**
	 * Export als...
	 */
	enum ExportType{
		/**
		 * @uml.property  name="pDF"
		 * @uml.associationEnd  
		 */
		PDF("pdf", "Pdf"), /**
		 * @uml.property  name="hTML"
		 * @uml.associationEnd  
		 */
		HTML("html","Html"), /**
		 * @uml.property  name="xML"
		 * @uml.associationEnd  
		 */
		XML("xml","Xml"), /**
		 * @uml.property  name="eXCEL"
		 * @uml.associationEnd  
		 */
		EXCEL("xls","Excel"), /**
		 * @uml.property  name="cSV"
		 * @uml.associationEnd  
		 */
		CSV("csv","Csv");
		
		ExportType(String pExtension, String pName){
			extension = pExtension;
			name = pName;
		}
		/**
		 * @return
		 * @uml.property  name="extension"
		 */
		public String getExtension(){
			return extension;
		}
		/**
		 * @return
		 * @uml.property  name="name"
		 */
		public String getName(){
			return name;
		}
		
		/**
		 * @uml.property  name="extension"
		 */
		private String extension;
		/**
		 * @uml.property  name="name"
		 */
		private String name;
	}
	
	public IReportEngine getReportEngine();
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
