package ch.jester.commonservices.api.importer;

public interface IImportHandlerEntry<IImportHandler> {
	public String getUIString();
	public Class<?> getProviderClass();
	public IImportHandler getService();
}
