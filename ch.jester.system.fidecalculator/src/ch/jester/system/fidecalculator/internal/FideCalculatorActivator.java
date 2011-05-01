package ch.jester.system.fidecalculator.internal;

import org.osgi.framework.BundleContext;

import ch.jester.common.activator.AbstractActivator;

/**
 * The activator class controls the plug-in life cycle
 */
public class FideCalculatorActivator  extends AbstractActivator {

	// The plug-in ID
	public static final String PLUGIN_ID = "ch.jester.system.fidecalculator"; //$NON-NLS-1$

	// The shared instance
	private static FideCalculatorActivator plugin;
	
	/**
	 * The constructor
	 */
	public FideCalculatorActivator() {
	}

	@Override
	public void startDelegate(BundleContext pContext) {
		plugin = this;
		getActivationContext().getLogger().info("Starting FIDE-Calculator Plugin");
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
	public static FideCalculatorActivator getDefault() {
		return plugin;
	}

}
