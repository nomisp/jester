package ch.jester.commonservices.api.importer;


public interface IWebImportHandlerEntry extends IImportHandlerEntry{
	/**
	 * Der Provider der Page
	 * @return
	 */
	public String getProviderName();
	/**
	 * @return die Id des zu adaptierenden Entries
	 */
	public String getImportHandlerId();
}
