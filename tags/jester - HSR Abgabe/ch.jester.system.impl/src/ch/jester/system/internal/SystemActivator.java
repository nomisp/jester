package ch.jester.system.internal;

import org.osgi.framework.BundleContext;

import ch.jester.common.activator.AbstractActivator;

/**
 * Bundle-Activator des System.impl Plugins
 *
 */
public class SystemActivator extends AbstractActivator {

	private static SystemActivator mActivator;
	
	@Override
	public void startDelegate(BundleContext context) {
		mActivator=this;
		getActivationContext().getLogger().info("Starting System Plugin");

	}
	@Override
	public void stopDelegate(BundleContext context) {
		
	}
	public static SystemActivator getInstance() {
		return mActivator;
	}
}
