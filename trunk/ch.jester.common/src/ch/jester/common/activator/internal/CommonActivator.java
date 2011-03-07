package ch.jester.common.activator.internal;

import org.osgi.framework.BundleContext;

import ch.jester.common.activator.AbstractActivator;
import ch.jester.ep.ExtensionPointChangeNotifier;

public class CommonActivator extends AbstractActivator{
	private static CommonActivator mInstance;
	ExtensionPointChangeNotifier notifier;
	public CommonActivator() {
		mInstance=this;
		//notifier = new ExtensionPointChangeNotifier("ch.jester.hibernate.helper","Configuration");
	
	}
	
	public static CommonActivator getInstance(){
		return mInstance;
	}
	
	@Override
	public void startDelegate(BundleContext pContext) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void stopDelegate(BundleContext pContext) {
		// TODO Auto-generated method stub
		
	}

}
