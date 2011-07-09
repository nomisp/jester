package ch.jester.socialmedia.facebook.internal;

import org.osgi.framework.BundleContext;
import org.slf4j.bridge.SLF4JBridgeHandler;

import ch.jester.common.ui.activator.AbstractUIActivator;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIActivator {

	public static final String PLUGIN_ID = "ch.jester.socialmedia.facebook"; //$NON-NLS-1$

	private static Activator activator;
	public static Activator getDefault() {
		return activator ;
	}

	@Override
	public void startDelegate(BundleContext pContext) {
		SLF4JBridgeHandler.install();
		activator=this;
		
	}

	@Override
	public void stopDelegate(BundleContext pContext) {
		// TODO Auto-generated method stub
		
	}

}
