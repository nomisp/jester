package ch.jester.commonservices.api.reportengine;

import java.io.File;

import ch.jester.commonservices.exceptions.ProcessingException;

public interface IReportResult {
	enum ExportType{
		PDF, HTML, XML;
	}
	public String getOutputName();
	public void setOutputName(String pOutputName);
	public boolean canExport(ExportType ex);
	public File export(ExportType ex) throws ProcessingException;
}
