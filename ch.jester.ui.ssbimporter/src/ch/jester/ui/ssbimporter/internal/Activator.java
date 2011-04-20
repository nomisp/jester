package ch.jester.ui.ssbimporter.internal;

import org.osgi.framework.BundleContext;

import ch.jester.common.ui.activator.AbstractUIActivator;

public class Activator extends AbstractUIActivator {
	private static Activator mInstance;
	public Activator() {
		mInstance=this;
	}

	public static Activator getInstance(){
		return mInstance;
	}
	
	@Override
	public void startDelegate(BundleContext pContext) {


	}

	@Override
	public void stopDelegate(BundleContext pContext) {

	}

}
