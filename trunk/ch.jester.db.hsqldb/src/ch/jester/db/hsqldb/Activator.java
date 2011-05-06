package ch.jester.db.hsqldb;

import org.osgi.framework.BundleContext;

import ch.jester.common.ui.activator.AbstractUIActivator;
import ch.jester.commonservices.api.logging.ILogger;

public class Activator extends AbstractUIActivator {
	private ILogger mLogger;
	private static Activator plugin;
	
	@Override
	public void startDelegate(BundleContext pContext) {
		mLogger = getActivationContext().getLogger();
		mLogger.info("Starting >HSQLDB-Plugin<");
		plugin = this;
	}

	@Override
	public void stopDelegate(BundleContext pContext) {
		mLogger.info("Stopping >HSQLDB-Plugin<");
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
