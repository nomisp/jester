package ch.jester.common.ui.internal;

import org.osgi.framework.BundleContext;

import ch.jester.common.ui.activator.AbstractUIActivator;
import ch.jester.common.ui.services.IEditorService;

public class CommonUIActivator extends AbstractUIActivator {
	private static CommonUIActivator mInstance;
	@Override
	public void startDelegate(BundleContext pContext) {
		mInstance=this;
		getActivationContext().getServiceUtil().registerService(IEditorService.class, new EditorService());
		
	}
	
	@Override
	public void stopDelegate(BundleContext pContext) {
		
	}
	
	/**
	 * @return die shared Instance
	 */
	public static  CommonUIActivator getDefault(){
		return mInstance;
	}

}
