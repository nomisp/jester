package ch.jester.importmanagerservice.manager;

import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;

import ch.jester.common.utility.ExtensionPointUtil;
import ch.jester.commonservices.api.importer.IImportHandler;
import ch.jester.commonservices.api.importer.IImportHandlerEntry;
import ch.jester.commonservices.api.importer.IImportManager;
import ch.jester.commonservices.api.importer.IWebImportAdapter;
import ch.jester.commonservices.api.importer.IWebImportHandlerEntry;
import ch.jester.commonservices.util.ServiceUtility;

public class WebAdapterHandlerEntry extends DefaultImportHandlerEntry implements IWebImportHandlerEntry{
	private boolean mInstalled;
	private ServiceUtility mServices = new ServiceUtility();
	public WebAdapterHandlerEntry(IWebImportAdapter pService){
		super(pService);
		
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public IImportHandler getService() {
		if(!mInstalled){
			List<IImportHandlerEntry> entries = mServices.getService(IImportManager.class).getRegistredEntries();
			String myId = getImportHandlerId();
			IImportHandlerEntry instanciatedHandler = null;
			for(IImportHandlerEntry entry:entries){
				String entryId = entry.getProperty("id");
				if(entryId!=null&&entryId.equals(myId)){
					//System.out.println(entryId);
					instanciatedHandler=entry;
					break;
				}
			}
			if(instanciatedHandler!=null){
				IWebImportAdapter entry =  (IWebImportAdapter) super.getService();
				entry.setIImportHandler(instanciatedHandler.getService());
				
				mInstalled=true;
		
			}else{
			
			IWebImportAdapter entry =  (IWebImportAdapter) super.getService();
			IConfigurationElement element = ExtensionPointUtil.getExtensionPointElement("ch.jester.commonservices.api", "ImportHandler", "id", getImportHandlerId());
			try {
				IImportHandler imphandler = (IImportHandler) element.createExecutableExtension("class");
				entry.setIImportHandler(imphandler);
			} catch (CoreException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			mInstalled=true;
			}
		}
		return super.getService();
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
