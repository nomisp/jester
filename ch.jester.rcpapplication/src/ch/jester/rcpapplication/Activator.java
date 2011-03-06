package ch.jester.rcpapplication;

import org.eclipse.jface.resource.ImageDescriptor;
import org.osgi.framework.BundleContext;

import ch.jester.common.ui.activator.AbstractUIActivator;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIActivator{
	@Override
	public void startDelegate(BundleContext pContext) {
		getActivationContext().getLogger().info("Starting >jester<");
	}

	@Override
	public void stopDelegate(BundleContext pContext) {
		getActivationContext().getLogger().info("Stopping >jester<");
	}
}
