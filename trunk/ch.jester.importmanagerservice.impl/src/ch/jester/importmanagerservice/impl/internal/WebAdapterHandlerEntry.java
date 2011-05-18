package ch.jester.importmanagerservice.impl.internal;

import org.eclipse.core.runtime.IConfigurationElement;

import ch.jester.common.utility.ExtensionPointUtil;
import ch.jester.commonservices.api.importer.IImportHandler;
import ch.jester.commonservices.api.importer.IImportHandlerEntry;
import ch.jester.commonservices.api.importer.IWebImportAdapter;
import ch.jester.commonservices.api.importer.IWebImportHandlerEntry;

public class WebAdapterHandlerEntry extends DefaultImportHandlerEntry implements IWebImportHandlerEntry{
	public WebAdapterHandlerEntry(IWebImportAdapter pService){
		super(pService);
	}

	@Override
	public String getImportHandlerId() {
		return getProperty("ImportHandlerId");
	}

	public String getShortType(){
		IConfigurationElement element = ExtensionPointUtil.getExtensionPointElement("ch.jester.commonservices.api", "ImportHandler", "id", getImportHandlerId());
		return element.getAttribute(IImportHandlerEntry.SHORTTYPE);
	}

	public String getDescription(){
		IConfigurationElement element = ExtensionPointUtil.getExtensionPointElement("ch.jester.commonservices.api", "ImportHandler", "id", getImportHandlerId());
		return element.getAttribute(IImportHandlerEntry.TYPEDESCRIPTION);
	}
	
	public String toString(){
		return getProviderName();
	}
	
	@Override
	public String getProviderName() {
		return getProperty("dataProviderDescription");
	}

}
