package ch.jester.importmanagerservice.manager;

import org.osgi.framework.BundleContext;

import ch.jester.common.activator.AbstractActivator;

/**
 * Einfacher Aktivator
 */
public class ImportManagerActivator extends AbstractActivator {
	
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
