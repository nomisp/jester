package ch.jester.common.reportengine;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.osgi.framework.Bundle;

import ch.jester.commonservices.api.reportengine.IReport;

public class DefaultReport implements IReport{
	private String mAlias, mVisibleName, mFileName;
	private Bundle mBundle;
	private File mInstalledFile;
	@Override
	public void setAlias(String pString) {
		mAlias = pString;
	}

	@Override
	public String getAlias() {
		return mAlias;
	}

	@Override
	public void setVisibleName(String pString) {
		mVisibleName=pString;
	}

	@Override
	public String getVisibleName() {
		return mVisibleName;
	}

	@Override
	public String getBundleFileName() {
		return mFileName;
	}

	@Override
	public void setBundleFileName(String pFilePath) {
		mFileName = pFilePath;
	}

	@Override
	public void setBundle(Bundle b) {
		mBundle = b;
		
	}

	@Override
	public InputStream getBundleFileAsStream() throws IOException {
		URL url = mBundle.getResource(mFileName);
		return url.openStream();
	}

	@Override
	public InputStream getInstalledFileAsStream() throws IOException {
		if(mInstalledFile==null){
			return getBundleFileAsStream();
		}
		return new FileInputStream(mInstalledFile);
	}

	@Override
	public void setInstalledFile(File pInstalled) {
		mInstalledFile=pInstalled;
	}

	@Override
	public File getInstalledFile() {
		return mInstalledFile;
	}

}
