package ch.jester.common.importer;

import ch.jester.commonservices.api.importer.IImportHandler;
import ch.jester.commonservices.api.importer.IImportHandlerEntry;

public class DefaultImportHandlerEntry<T extends IImportHandler> implements IImportHandlerEntry<T>{
	private String mUIString;
	private Class<T> mHandlerClass;
	private T mService;
	public DefaultImportHandlerEntry(String pUIString, Class<T> pHandlerClass){
		mUIString=pUIString;
		mHandlerClass=pHandlerClass;
	}
	public DefaultImportHandlerEntry(String pUIString, T pService){
		mUIString=pUIString;
		mHandlerClass=(Class<T>) pService.getClass();
		mService = pService;
	}
	@Override
	public String getUIString() {
		return mUIString;
	}
	@Override
	public Class<T> getProviderClass() {
		return mHandlerClass;
	}
	@Override
	public T getService() {
		if(mService==null){
			loadService();
		}
		return mService;
	}
	private void loadService() {

	}
	


}
