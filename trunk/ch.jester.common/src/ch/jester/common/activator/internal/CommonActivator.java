package ch.jester.common.activator.internal;

import org.osgi.framework.BundleContext;

import ch.jester.common.activator.AbstractActivator;
import ch.jester.ep.ExtensionPointChangeNotifier;

/**
 * @author  t117221
 */
public class CommonActivator extends AbstractActivator{
	/**
	 * @uml.property  name="mInstance"
	 * @uml.associationEnd  
	 */
	private static CommonActivator mInstance;
	/**
	 * @uml.property  name="notifier"
	 * @uml.associationEnd  
	 */
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
