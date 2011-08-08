package ch.jester.persistence;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

/**
 * StandardActivator
 *
 */
public class Activator implements BundleActivator {

	private static BundleContext context;

	static BundleContext getContext() {
		return context;
	}

	public void start(BundleContext bundleContext) throws Exception {
		Activator.context = bundleContext;
	}

	public void stop(BundleContext bundleContext) throws Exception {
		Activator.context = null;
	}

}
