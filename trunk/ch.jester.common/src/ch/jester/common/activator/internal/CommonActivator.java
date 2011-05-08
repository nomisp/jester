package ch.jester.common.activator.internal;

import org.osgi.framework.BundleContext;

import ch.jester.common.activator.AbstractActivator;
import ch.jester.ep.ExtensionPointChangeNotifier;

public class CommonActivator extends AbstractActivator{
	private static CommonActivator mInstance;
	ExtensionPointChangeNotifier notifier;
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
