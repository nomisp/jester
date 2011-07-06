package ch.jester.rankingsystem.buchholz.internal;

import org.osgi.framework.BundleContext;

import ch.jester.common.activator.AbstractActivator;

/**
 * The activator class controls the plug-in life cycle
 */
public class BuchholzActivator extends AbstractActivator {

	// The plug-in ID
	public static final String PLUGIN_ID = "ch.jester.rankingsystem.buchholz"; //$NON-NLS-1$
	public static final String RANKINGSYSTEM = "Buchholz";

	// The shared instance
	private static BuchholzActivator plugin;
	
	/**
	 * The constructor
	 */
	public BuchholzActivator() {
	}

	@Override
	public void startDelegate(BundleContext pContext) {
		plugin = this;
		getActivationContext().getLogger().info("Starting Buchholz Ranking-System Plugin");
	}

	@Override
	public void stopDelegate(BundleContext pContext) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static BuchholzActivator getDefault() {
		return plugin;
	}

}
