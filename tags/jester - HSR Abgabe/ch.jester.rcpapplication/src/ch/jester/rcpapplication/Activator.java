package ch.jester.rcpapplication;


import org.osgi.framework.BundleContext;

import ch.jester.common.ui.activator.AbstractUIActivator;
import ch.jester.commonservices.api.logging.ILogger;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIActivator{
	
	private static Activator plugin;
	private ILogger mLogger;
	@Override
	public void startDelegate(BundleContext pContext) {
		mLogger = getActivationContext().getLogger();
		mLogger.info("Starting >jester<");
		plugin = this;

	}

	@Override
	public void stopDelegate(BundleContext pContext) {
		mLogger.info("Stopping >jester<");
		plugin = null;
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static Activator getDefault() {
		return plugin;
	}
}
