package ch.jester.ui.importer.internal;

import ch.jester.common.ui.adapters.StructuredContentProviderAdapter;
import ch.jester.commonservices.api.importer.IImportManager;
import ch.jester.commonservices.util.ServiceUtility;

public class WebImportAdapterContentProvider extends StructuredContentProviderAdapter{
	ServiceUtility mService = new ServiceUtility();
	private Controller mController;
	public WebImportAdapterContentProvider(Controller pController){
		mController = pController;
	}
	@Override
	public Object[] getElements(Object inputElement) {
		IImportManager manager = mService.getService(IImportManager.class);
		if(manager==null){
			return new Object[]{};
		}
		return mController.getHandlersForCurrentMode().toArray();
	}
	@Override
	public void dispose() {
		mService.closeAllTrackers();
		super.dispose();
	}
}