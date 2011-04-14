package ch.jester.rcpapplication;

import org.osgi.framework.BundleContext;

import ch.jester.common.ui.activator.AbstractUIActivator;
import ch.jester.commonservices.api.components.IComponentService;
import ch.jester.commonservices.api.importer.IImportManager;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIActivator{
	
	private static Activator plugin;
	
	@Override
	public void startDelegate(BundleContext pContext) {
		getActivationContext().getLogger().info("Starting >jester<");
		IComponentService<?> manager = getActivationContext().getService(IImportManager.class);	
		getActivationContext().getLogger().info("ImportManager is "+manager);
		plugin = this;
	}

	@Override
	public void stopDelegate(BundleContext pContext) {
		getActivationContext().getLogger().info("Stopping >jester<");
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
