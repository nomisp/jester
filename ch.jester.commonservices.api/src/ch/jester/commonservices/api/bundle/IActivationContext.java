package ch.jester.commonservices.api.bundle;

import org.osgi.framework.BundleContext;

import ch.jester.commonservices.api.logging.ILogger;
import ch.jester.commonservices.util.ServiceUtility;

/**
 * Interface dass Zugriff auf div. interessante Objekte des Bundles/Contextes
 * gewährt.
 * 
 * @param <V>
 */
public interface IActivationContext<V> {

	/**
	 * @return den zum Context gehörenden Activator
	 */
	public V getActivator();

	/**
	 * getter für die PluginId
	 * @return die PluginId des Bundles
	 */
	public String getPluginId();

	/**
	 * Zugriff auf den BundleContext
	 * 
	 * @return den BundleContext zum Bundle
	 */
	public BundleContext getBundleContext();

	/**
	 * Zugriff auf ein ServiceUtility
	 * @return aServiceUtility
	 */
	public ServiceUtility getServiceUtil();

	/**
	 * @param Klasse
	 *            für den ILoggernamen
	 * @return eine ILogger Implementation
	 */
	public ILogger getLogger(Class<?> t);

	/**
	 * Zugriff auf den BundleLogger
	 * 
	 * @return eine ILogger Implementation
	 */
	public ILogger getLogger();

	/**
	 * Konventional Methode <br>
	 * identisch zu {@link IActivationContext#getServiceUtil()#getService(Class)}
	 * @param <T>
	 * @param pClass
	 * @return einService vom Typ T
	 */
	public <T> T getService(Class<T> pClass);

}