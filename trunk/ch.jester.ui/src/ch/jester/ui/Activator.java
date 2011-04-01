package ch.jester.ui;

import org.osgi.framework.BundleContext;

import ch.jester.common.activator.AbstractActivator;

public class Activator extends AbstractActivator{

	private static Activator mInstance;

	public static Activator getDefault(){
		return mInstance;
	}

	@Override
	public void startDelegate(BundleContext pContext) {
		mInstance=this;
	}

	@Override
	public void stopDelegate(BundleContext pContext) {
		// TODO Auto-generated method stub
		
	}

}
