package ch.jester.common.importer;

import java.util.List;

import ch.jester.commonservices.api.importer.IImportHandler;
import ch.jester.commonservices.api.importer.IImportHandlerEntry;
import ch.jester.commonservices.api.importer.IImportManager;

public class DefaultImportManager implements IImportManager{

	@Override
	public List<IImportHandlerEntry<? extends IImportHandler>> getRegistredImportHandlers() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object doImport(
			IImportHandlerEntry<? extends IImportHandler> pEntry,
			Object pObjectToImport) {
		// TODO Auto-generated method stub
		return null;
	}

}
