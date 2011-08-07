package ch.jester.system.vollrundig.internal;

import org.osgi.framework.BundleContext;

import ch.jester.common.activator.AbstractActivator;

/**
 * Activator für das Vollrundige-Plugin
 *
 */
public class VollrundigSystemActivator extends AbstractActivator {
	
	public static final String PLUGIN_ID = "ch.jester.system.vollrundig";
	
	private static VollrundigSystemActivator mActivator;

	@Override
	public void startDelegate(BundleContext pContext) {
		mActivator = this;
		getActivationContext().getLogger().info("Starting Vollrundig Plugin");
	}

	@Override
	public void stopDelegate(BundleContext pContext) {
	}
	
	public static VollrundigSystemActivator getDefault() {
		return mActivator;
	}
}
