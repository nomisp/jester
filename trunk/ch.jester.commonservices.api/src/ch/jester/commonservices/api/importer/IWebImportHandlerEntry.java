package ch.jester.commonservices.api.importer;


public interface IWebImportHandlerEntry extends IImportHandlerEntry{
	public String getProviderName();
	public String getImportHandlerId();
}
