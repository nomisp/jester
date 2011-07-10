package ch.jester.importmanagerservice.manager;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;

import ch.jester.common.components.AbstractEPComponent;
import ch.jester.commonservices.api.importer.IImportHandler;
import ch.jester.commonservices.api.importer.IImportHandlerEntry;
import ch.jester.commonservices.api.importer.IImportManager;
import ch.jester.commonservices.api.importer.IWebImportAdapter;
import ch.jester.commonservices.api.importer.IWebImportHandlerEntry;
import ch.jester.commonservices.api.preferences.IPreferenceManager;
import ch.jester.commonservices.api.preferences.IPreferenceManagerProvider;
import ch.jester.commonservices.api.preferences.IPreferenceRegistration;
import ch.jester.commonservices.util.ServiceUtility;

/**
 * Defaultimplementation<br>
 * Dieser Manager horcht auf Einträge/Änderungen am ExtensionPoint ch.jester.commonservices.api.ImportHandler
 *
 */
@SuppressWarnings("rawtypes")
public class DefaultImportManager extends AbstractEPComponent<IImportHandlerEntry, IImportHandler> implements IImportManager, IPreferenceManagerProvider{
	ServiceUtility mServices = new ServiceUtility();
	public DefaultImportManager(){
		super(IImportHandler.class, 
				ImportManagerActivator.getInstance().getActivationContext(),
				"ch.jester.commonservices.api", 
				"ImportHandler");
		mServices.getService(IPreferenceRegistration.class).registerPreferenceProvider(this);
	}


	@Override
	public Object doImport(IImportHandlerEntry pEntry,
			InputStream pObjectToImport, IProgressMonitor pMonitor) {
		return pEntry.getService().handleImport(pObjectToImport, pMonitor);
	}

	@Override
	public List<IImportHandlerEntry> filter(IImportFilter pFilter) {
		List<IImportHandlerEntry> filtredEntries = new ArrayList<IImportHandlerEntry>();
		for(IImportHandlerEntry e:getRegistredEntries()){
			if(pFilter.match(e.getShortType())){
				filtredEntries.add(e);
			}
		}
		return filtredEntries;
	}

	class DefaultFilter implements IImportFilter{
		String mFilter;
		public DefaultFilter(String pFilter){
			mFilter=pFilter.toLowerCase();
		}
		@Override
		public boolean match(String p) {
			return mFilter.matches(p.replaceAll("\\*.", ""));
		}
		
	}

	@Override
	public IImportFilter createMatchingExtension(String pString) {
		return new DefaultFilter(pString);
	}

	@Override
	protected IImportHandlerEntry createEntry(IImportHandler o) {
		if(o instanceof IWebImportAdapter){
			return new WebAdapterHandlerEntry((IWebImportAdapter) o);
		}
		return new DefaultImportHandlerEntry(o);
	}


	@Override
	public IPreferenceManager getPreferenceManager(String pId) {
		for(IImportHandlerEntry e:getRegistredEntries()){
			if(e instanceof IWebImportHandlerEntry){
				IWebImportHandlerEntry entry= (IWebImportHandlerEntry) e;
				IWebImportAdapter adapter = (IWebImportAdapter) entry.getService();
				IPreferenceManager pm = adapter.getPreferenceManager(pId);
				if(pm!=null){
					return pm;
				}
			}
		}
		return null;
	}


}
