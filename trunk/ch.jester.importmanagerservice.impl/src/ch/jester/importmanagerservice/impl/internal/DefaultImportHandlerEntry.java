package ch.jester.importmanagerservice.impl.internal;

import ch.jester.commonservices.api.importer.IImportHandler;
import ch.jester.commonservices.api.importer.IImportHandlerEntry;

/**
 * Defaultimplementation
 *
 */
public class DefaultImportHandlerEntry implements IImportHandlerEntry{
	private IImportHandler mService;
	public DefaultImportHandlerEntry(IImportHandler pService){
		mService = pService;
	}
	@Override
	public IImportHandler getService() {
		return mService;
	}
	@Override
	public String getShortType() {
		return mService.getProperty(IImportHandlerEntry.SHORTTYPE);
	}
	@Override
	public String getDescription() {
		return mService.getProperty(IImportHandlerEntry.TYPEDESCRIPTION);
	}

	public String toString(){
		return getDescription();
	}


}
