package ch.jester.system.vollrundig.internal;

import org.osgi.framework.BundleContext;

import ch.jester.common.activator.AbstractActivator;

public class VollrundigSystemActivator extends AbstractActivator {
	
	private static VollrundigSystemActivator mActivator;

	@Override
	public void startDelegate(BundleContext pContext) {
		mActivator = this;
		getActivationContext().getLogger().info("Starting Vollrundig Plugin");
	}

	@Override
	public void stopDelegate(BundleContext pContext) {
		// TODO Auto-generated method stub
		
	}
	
	public static VollrundigSystemActivator getInstance() {
		return mActivator;
	}
}
