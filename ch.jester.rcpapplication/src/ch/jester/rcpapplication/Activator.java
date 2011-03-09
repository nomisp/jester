package ch.jester.rcpapplication;

import org.osgi.framework.BundleContext;

import ch.jester.common.ui.activator.AbstractUIActivator;
import ch.jester.commonservices.api.importer.IImportManager;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIActivator{
	@Override
	public void startDelegate(BundleContext pContext) {
		getActivationContext().getLogger().info("Starting >jester<");
		IImportManager manager = getActivationContext().getService(IImportManager.class);	
		getActivationContext().getLogger().info("ImportManager is "+manager);
	}

	@Override
	public void stopDelegate(BundleContext pContext) {
		getActivationContext().getLogger().info("Stopping >jester<");
	}
}
