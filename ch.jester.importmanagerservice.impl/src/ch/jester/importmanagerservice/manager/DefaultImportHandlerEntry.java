package ch.jester.importmanagerservice.manager;

import ch.jester.common.components.EPEntry;
import ch.jester.commonservices.api.importer.IImportHandler;
import ch.jester.commonservices.api.importer.IImportHandlerEntry;

/**
 * Defaultimplementation
 *
 */
@SuppressWarnings("rawtypes")
public class DefaultImportHandlerEntry extends EPEntry<IImportHandler> implements IImportHandlerEntry{

	public DefaultImportHandlerEntry(IImportHandler pService){
		super(pService);
	}

	@Override
	public String getShortType() {
		return getProperty(IImportHandlerEntry.SHORTTYPE);
	}
	@Override
	public String getDescription() {
		return getProperty(IImportHandlerEntry.TYPEDESCRIPTION);
	}

	public String toString(){
		return getDescription();
	}


}
