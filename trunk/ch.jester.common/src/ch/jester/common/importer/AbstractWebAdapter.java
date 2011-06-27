package ch.jester.common.importer;

import java.io.InputStream;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;

import ch.jester.common.web.ExtensionFilter;
import ch.jester.common.web.LinkFilter;
import ch.jester.common.web.PageReader;
import ch.jester.commonservices.api.importer.IImportHandler;
import ch.jester.commonservices.api.importer.ILink;
import ch.jester.commonservices.api.importer.IWebImportAdapter;
import ch.jester.commonservices.api.logging.ILogger;
import ch.jester.commonservices.api.preferences.IPreferenceManager;
import ch.jester.commonservices.api.preferences.IPreferencePropertyChanged;
import ch.jester.commonservices.api.preferences.IPreferenceRegistration;
import ch.jester.commonservices.util.ServiceUtility;


/**
 * @author  t117221
 */
public abstract class AbstractWebAdapter implements IWebImportAdapter, IPreferencePropertyChanged{
	private String mDownloadAddress;
	protected List<ILink> mLinkList;
	/**
	 * @uml.property  name="linkfilter"
	 * @uml.associationEnd  
	 */
	protected LinkFilter linkfilter;
	/**
	 * @uml.property  name="reader"
	 * @uml.associationEnd  
	 */
	protected PageReader reader = new PageReader();
	/**
	 * @uml.property  name="mPrefManager"
	 * @uml.associationEnd  
	 */
	protected IPreferenceManager mPrefManager;
	/**
	 * @uml.property  name="mLogger"
	 * @uml.associationEnd  
	 */
	protected ILogger mLogger;
	/**
	 * @uml.property  name="mService"
	 * @uml.associationEnd  
	 */
	protected ServiceUtility mService = new ServiceUtility();
	/**
	 * @uml.property  name="mAdaptedHandler"
	 * @uml.associationEnd  
	 */
	@SuppressWarnings("rawtypes")
	private IImportHandler mAdaptedHandler;

	public AbstractWebAdapter(){
		mPrefManager = mService.getService(IPreferenceRegistration.class).createManager();
		mPrefManager.addListener(AbstractWebAdapter.this);
		
	}
	
	@Override
	public Object handleImport(InputStream pInputStream, IProgressMonitor pMonitor) {
		return mAdaptedHandler.handleImport(pInputStream, pMonitor);
	}

	@Override
	public void setIImportHandler(@SuppressWarnings("rawtypes") IImportHandler pAdaptedHandler) {
		mAdaptedHandler=pAdaptedHandler;
	}
	@SuppressWarnings("rawtypes")
	@Override
	public Object getAdapter(Class adapter) {
		return mAdaptedHandler.getAdapter(adapter);
	}
	public void setDownloadAddress(String pAddress){
		mDownloadAddress=pAddress;
	}
	public String getDownloadAddress(){
		return mDownloadAddress;
	}
	
	@Override
	public IPreferenceManager getPreferenceManager(String pId) {
		if(mPrefManager.getId().equals(pId)){
			return mPrefManager;
		}
		return null;
	}
	
	protected ExtensionFilter createFilterFromProperties() {
		LinkFilter f = new LinkFilter();
		String pattern = mPrefManager.getPropertyByInternalKey("pattern").getValue().toString();
		String exFilter = mPrefManager.getPropertyByInternalKey("extensionFilter").getValue().toString();
		int grpName = (Integer) mPrefManager.getPropertyByInternalKey("groupName").getValue();
		int grpURL = (Integer) mPrefManager.getPropertyByInternalKey("groupURL").getValue();
		f.setPattern(pattern, grpName, grpURL);
		linkfilter = f;
		ExtensionFilter ef = new ExtensionFilter(exFilter, f);
		reader.setFilter(ef);
		//System.out.println("New Filter created: "+pattern+" "+exFilter+" "+grpName+" "+grpURL);
		return ef;
	}
}