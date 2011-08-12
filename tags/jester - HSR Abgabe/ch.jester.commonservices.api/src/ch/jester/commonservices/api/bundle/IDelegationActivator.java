package ch.jester.commonservices.api.bundle;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;


/**
 * Subclassing Delegate für Aktivatoren
 * @param <T>
 */
public interface IDelegationActivator<T> extends IActivationContextProvider<T>,
		BundleActivator {
	/**
	 * Delegation an die implementierende Klasse
	 * 
	 * @param pContext
	 *            der BundleContext
	 */
	public void startDelegate(BundleContext pContext);

	/**
	 * Delegation an die implementierende Klasse
	 * 
	 * @param pContext
	 *            der BundleContext
	 */
	public void stopDelegate(BundleContext pContext);
	
}
