package ch.jester.reportengine.impl.internal;

import org.osgi.framework.BundleContext;

import ch.jester.common.ui.activator.AbstractUIActivator;

/**
 * @author  t117221
 */
public class Activator extends AbstractUIActivator {

	/**
	 * @uml.property  name="instance"
	 * @uml.associationEnd  
	 */
	private static Activator instance;
	
	public static Activator getDefault(){
		return instance;
	}
	
	@Override
	public void startDelegate(BundleContext pContext) {
		instance=this;

	}

	@Override
	public void stopDelegate(BundleContext pContext) {
		// TODO Auto-generated method stub

	}

}
