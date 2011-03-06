package ch.jester.common.test.internal;

import org.osgi.framework.BundleActivator;

import ch.jester.common.activator.IActivationContext;

/**
 * Klasse um den Zugriff für TestCases auf den Testactivator zu 
 * vereinfachen.
 */
public abstract class ActivatorProviderForTestCase {
	/**
	 * Identisch zu TestActivator.getActivator()
	 * @return den TestActivator
	 */
	public TestActivator getActivator(){
		return TestActivator.getActivator();
	}
	
	public IActivationContext<BundleActivator> getActivationContext(){
		return TestActivator.getActivator().getActivationContext();
	}

}
