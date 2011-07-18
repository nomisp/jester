package ch.jester.ui.importer.internal;

import java.util.List;

import ch.jester.common.ui.adapters.StructuredContentProviderAdapter;
import ch.jester.commonservices.api.importer.IImportHandlerEntry;
import ch.jester.commonservices.api.importer.IImportManager;
import ch.jester.commonservices.util.ServiceUtility;

public class ImportHandlerContentProvider extends StructuredContentProviderAdapter {
	private ServiceUtility mService = new ServiceUtility();
	private Controller mController;
	public ImportHandlerContentProvider(Controller pController){
		mController=  pController;
	}
	@Override
	public Object[] getElements(Object inputElement) {
		IImportManager manager = mService.getService(IImportManager.class);
		if(manager==null||inputElement==null){
			return new Object[]{};
		}
		List<? extends IImportHandlerEntry> handlers = mController.getHandlersForCurrentMode();
		if(inputElement instanceof String){
			String input = (String) inputElement;
			if(input.lastIndexOf(".")==-1){
				return new Object[]{};
			}
			String extension = input.substring(input.lastIndexOf(".")+1);
			
			List<IImportHandlerEntry> allWithExtensions =  manager.filter(manager.createMatchingExtension(extension));
			allWithExtensions.retainAll(handlers);
			return allWithExtensions.toArray();
		}else{
			handlers = manager.getRegistredEntries();
		}
		return handlers.toArray();
	}
	
	@Override
	public void dispose() {
		mService.closeAllTrackers();
		super.dispose();
	}
}