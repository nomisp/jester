package ch.jester.system.swiss.simple.internal;

import org.osgi.framework.BundleContext;

import ch.jester.common.activator.AbstractActivator;

public class SwissSimpleSystemActivator extends AbstractActivator {
	public static final String PLUGIN_ID = "ch.jester.system.swiss.simple";
	
	private static SwissSimpleSystemActivator mActivator;

	@Override
	public void startDelegate(BundleContext pContext) {
		mActivator = this;
		getActivationContext().getLogger().info("Starting Swiss (Simple) Pairing Plugin");
	}

	@Override
	public void stopDelegate(BundleContext pContext) {
		// TODO Auto-generated method stub
		
	}
	
	public static SwissSimpleSystemActivator getDefault() {
		return mActivator;
	}
}
