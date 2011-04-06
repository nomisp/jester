package ch.jester.common.ui.internal;

import org.osgi.framework.BundleContext;

import ch.jester.common.ui.activator.AbstractUIActivator;
import ch.jester.common.ui.services.IEditorService;

public class Activator extends AbstractUIActivator {
	private static Activator mInstance;
	@Override
	public void startDelegate(BundleContext pContext) {
		mInstance=this;
		getActivationContext().getServiceUtil().registerService(IEditorService.class, new EditorService());
		
	}
	public static  Activator getDefault(){
		return mInstance;
	}
	
	@Override
	public void stopDelegate(BundleContext pContext) {
		// TODO Auto-generated method stub
		
	}

}
