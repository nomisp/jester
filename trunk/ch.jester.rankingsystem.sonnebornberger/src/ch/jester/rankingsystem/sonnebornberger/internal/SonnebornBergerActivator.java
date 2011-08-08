package ch.jester.rankingsystem.sonnebornberger.internal;

import org.osgi.framework.BundleContext;

import ch.jester.common.activator.AbstractActivator;

/**
 * StandardActivator
 *
 */
public class SonnebornBergerActivator extends AbstractActivator {

	// The plug-in ID
	public static final String PLUGIN_ID = "ch.jester.rankingsystem.sonnebornberger"; //$NON-NLS-1$
	// The shared instance
	private static SonnebornBergerActivator plugin;
	
	@Override
	public void startDelegate(BundleContext pContext) {
		plugin = this;
		getActivationContext().getLogger().info("Starting SonnebornBerger Ranking-System Plugin");
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
	public static SonnebornBergerActivator getDefault() {
		return plugin;
	}
}
