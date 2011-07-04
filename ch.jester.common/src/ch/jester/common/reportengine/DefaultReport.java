package ch.jester.common.reportengine;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import ch.jester.commonservices.api.reportengine.IReport;

public class DefaultReport implements IReport{

	protected String mAlias;
	protected String mVisibleName;
	protected File mInstalledFile;
	protected Class<?> mInputBeanClass;
	public DefaultReport() {
		super();
	}

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
	public InputStream getInstalledFileAsStream() throws IOException {
/*		if(mInstalledFile==null){
			return getBundleFileAsStream();
		}*/
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

	@Override
	public void setInputBeanClass(Class<?> pClass) {
		mInputBeanClass = pClass;
	}
	
	@Override
	public Class<?> getInputBeanClass() {
		return mInputBeanClass;
	}

}