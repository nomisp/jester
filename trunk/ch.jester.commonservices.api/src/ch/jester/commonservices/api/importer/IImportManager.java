package ch.jester.commonservices.api.importer;

import java.util.List;

public interface IImportManager {
	public List<IImportHandlerEntry<? extends IImportHandler>> getRegistredImportHandlers();
	public Object doImport(IImportHandlerEntry<? extends IImportHandler> pEntry, Object pObjectToImport);
}
