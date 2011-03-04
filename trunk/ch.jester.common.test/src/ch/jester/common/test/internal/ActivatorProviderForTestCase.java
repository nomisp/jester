package ch.jester.common.test.internal;

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

}
