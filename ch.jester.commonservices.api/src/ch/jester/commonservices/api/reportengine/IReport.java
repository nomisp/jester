package ch.jester.commonservices.api.reportengine;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.osgi.framework.Bundle;

public interface IReport {
	public void setAlias(String pString);
	public String getAlias();
	public void setVisibleName(String pString);
	public String getVisibleName();
	public String getBundleFileName();
	public void setBundleFileName(String pFileName);
	public InputStream getBundleFileAsStream() throws IOException;
	public void setInstalledFile(File pInstalled);
	public File getInstalledFile();
	public InputStream getInstalledFileAsStream() throws IOException;
	public void setBundle(Bundle b);
}
