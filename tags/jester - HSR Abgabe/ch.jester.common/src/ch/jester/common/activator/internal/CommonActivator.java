package ch.jester.common.activator.internal;

import org.osgi.framework.BundleContext;

import ch.jester.common.activator.AbstractActivator;

/**
 * Aktivator des Common Packages
 *
 */
public class CommonActivator extends AbstractActivator{
	private static CommonActivator mInstance;
	public CommonActivator() {
		mInstance=this;
	}
	
	public static CommonActivator getInstance(){
		return mInstance;
	}
	
	@Override
	public void startDelegate(BundleContext pContext) {
		
	}

	@Override
	public void stopDelegate(BundleContext pContext) {
		
	}

}
