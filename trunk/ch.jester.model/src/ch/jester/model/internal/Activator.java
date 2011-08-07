package ch.jester.model.internal;

import org.osgi.framework.BundleContext;

import ch.jester.common.activator.AbstractActivator;

/**
 * Bundle Activator
 *
 */
public class Activator extends AbstractActivator{
	private static Activator mInstance;
	
	public static Activator getInstance(){
		return mInstance;
	}
	@Override
	public void startDelegate(BundleContext pContext) {
		mInstance=this;
	}

	@Override
	public void stopDelegate(BundleContext pContext) {
	}

}
