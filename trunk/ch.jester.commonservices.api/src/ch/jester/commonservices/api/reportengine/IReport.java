package ch.jester.commonservices.api.reportengine;

import java.io.IOException;
import java.io.InputStream;

import org.osgi.framework.Bundle;

public interface IReport {
	public void setAlias(String pString);
	public String getAlias();
	public void setVisibleName(String pString);
	public String getVisibleName();
	public String getFileName();
	public void setFileName(String pFileName);
	public InputStream getFileAsStream() throws IOException;
	public void setBundle(Bundle b);
}
