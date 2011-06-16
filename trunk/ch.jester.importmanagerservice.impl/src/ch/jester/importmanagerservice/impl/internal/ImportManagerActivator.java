package ch.jester.importmanagerservice.impl.internal;

import org.osgi.framework.BundleContext;

import ch.jester.common.activator.AbstractActivator;

/**
 * Einfacher Aktivator
 */
public class ImportManagerActivator extends AbstractActivator {
	
	/**
	 * @uml.property  name="mActivator"
	 * @uml.associationEnd  
	 */
	private static ImportManagerActivator mActivator;

	@Override
	public void startDelegate(BundleContext context) {
		mActivator=this;
		getActivationContext().getLogger().info("Starting ImportManager Plugin");

	}
	@Override
	public void stopDelegate(BundleContext context) {
		// TODO Auto-generated method stub
		
	}
	public static ImportManagerActivator getInstance() {
		return mActivator;
	}

	

}
