package ch.jester.common.reportengine;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.osgi.framework.Bundle;

import ch.jester.commonservices.api.reportengine.IReport;

public class DefaultReport implements IReport{
	private String mAlias, mVisibleName, mFilePath;
	private Bundle mBundle;
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
	public String getFileName() {
		return mFilePath;
	}

	@Override
	public void setFileName(String pFilePath) {
		mFilePath = pFilePath;
	}

	@Override
	public void setBundle(Bundle b) {
		mBundle = b;
		
	}

	@Override
	public InputStream getFileAsStream() throws IOException {
		URL url = mBundle.getResource(mFilePath);
		return url.openStream();
	}

}
