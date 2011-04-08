package ch.jester.db.hsqldb;

import org.osgi.framework.BundleContext;

import ch.jester.common.ui.activator.AbstractUIActivator;

public class Activator extends AbstractUIActivator {

	private static Activator plugin;
	
	@Override
	public void startDelegate(BundleContext pContext) {
		getActivationContext().getLogger().info("Starting >HSQLDB-Plugin<");
		plugin = this;
	}

	@Override
	public void stopDelegate(BundleContext pContext) {
		getActivationContext().getLogger().info("Stopping >HSQLDB-Plugin<");
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
