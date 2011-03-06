package ch.jester.commonservices.api.importer;

public interface IImportHandlerEntry<T extends IImportHandler> {
	public String getUIString();
	public Class<T> getProviderClass();
	public T getService();
}
