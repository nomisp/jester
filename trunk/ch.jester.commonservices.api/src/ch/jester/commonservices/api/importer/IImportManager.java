package ch.jester.commonservices.api.importer;

import java.util.List;

import org.osgi.service.component.ComponentContext;

public interface IImportManager {
	public List<IImportHandlerEntry<IImportHandler>> getRegistredImportHandlers();
	public Object doImport(IImportHandlerEntry<IImportHandler> pEntry, Object pObjectToImport);

	public void start(ComponentContext o);
	public void stop(ComponentContext o);
	
	public  void bind(IImportHandler o);
	public  void unbind(IImportHandler o);
}