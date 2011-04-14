package ch.jester.importmanagerservice.impl.internal;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;

import ch.jester.common.components.AbstractEPComponent;
import ch.jester.commonservices.api.importer.IImportHandler;
import ch.jester.commonservices.api.importer.IImportHandlerEntry;
import ch.jester.commonservices.api.importer.IImportManager;

/**
 * Defaultimplementation<br>
 * Dieser Manager horcht auf Einträge/Änderungen am ExtensionPoint ch.jester.commonservices.api.ImportHandler
 *
 */
public class DefaultImportManager extends AbstractEPComponent<IImportHandlerEntry, IImportHandler> implements IImportManager{
	public DefaultImportManager(){
		super(IImportHandler.class, 
				ImportManagerActivator.getInstance().getActivationContext(),
				"ch.jester.commonservices.api", 
				"ImportHandler");
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
	protected DefaultImportHandlerEntry createEntry(IImportHandler o) {
		return new DefaultImportHandlerEntry(o);
	}


}
