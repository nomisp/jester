package ch.jester.ui.tournament.internal;

import org.osgi.framework.BundleContext;

import ch.jester.common.ui.activator.AbstractUIActivator;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIActivator {

	// The plug-in ID
	public static final String PLUGIN_ID = "ch.jester.ui.tournament"; //$NON-NLS-1$

	// The shared instance
	private static Activator plugin;
	
	/**
	 * The constructor
	 */
	public Activator() {
		plugin = this;
	}

	public void startDelegate(BundleContext context) {
		
	}

	public void stopDelegate(BundleContext context) {
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