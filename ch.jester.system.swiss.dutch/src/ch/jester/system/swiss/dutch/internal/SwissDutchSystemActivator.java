package ch.jester.system.swiss.dutch.internal;

import org.osgi.framework.BundleContext;

import ch.jester.common.activator.AbstractActivator;

public class SwissDutchSystemActivator extends AbstractActivator {
	public static final String PLUGIN_ID = "ch.jester.system.swiss.dutch";
	
	private static SwissDutchSystemActivator mActivator;

	@Override
	public void startDelegate(BundleContext pContext) {
		mActivator = this;
		getActivationContext().getLogger().info("Starting Swiss (Dutch) Pairing Plugin");
	}

	@Override
	public void stopDelegate(BundleContext pContext) {
		// TODO Auto-generated method stub
		
	}
	
	public static SwissDutchSystemActivator getDefault() {
		return mActivator;
	}
}
