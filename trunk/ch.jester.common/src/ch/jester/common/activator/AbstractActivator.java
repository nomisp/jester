package ch.jester.common.activator;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import ch.jester.common.utility.CallerUtility;
import ch.jester.common.utility.ServiceUtility;
import ch.jester.commonservices.api.ILogger;
import ch.jester.commonservices.api.ILoggerFactory;

/**
 * Abstracter Activator mit Hilfsmethoden für Logging & Services
 * 
 */
public abstract class AbstractActivator implements BundleActivator {
	protected BundleContext mContext;
	private ServiceUtility mServiceUtility;

	@Override
	public void start(BundleContext context) throws Exception {
		mContext = context;
		mServiceUtility = new ServiceUtility(mContext);
		startDelegate(context);
	}

	/**
	 * Zugriff auf den BundleContext
	 * 
	 * @return den BundleContext zum Bundle
	 */
	public BundleContext getBundleContext() {
		return mContext;
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		stopDelegate(context);
		mServiceUtility.closeAllTrackers();
		mContext = null;

	}

	/**
	 * @return ServiceUtility
	 */
	public ServiceUtility getServiceUtil() {
		return mServiceUtility;
	}

	/**
	 * @param Klasse
	 *            für den ILogger
	 * @return eine ILogger Implementation
	 */
	public ILogger getLogger(Class<?> t) {
		ILoggerFactory loggerFactory = null;
		loggerFactory = getService(ILoggerFactory.class);

		return loggerFactory.getLogger(t);
	}

	/**
	 * Zugriff auf den BundleLogger
	 * 
	 * @return eine ILogger Implementation
	 */
	public ILogger getLogger() {
		String callingClassName = CallerUtility.getCaller(2).getCallerClass();
		Class<?> caller;
		try {
			caller = mContext.getBundle().loadClass(callingClassName);
			return getLogger(caller);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * @param <T>
	 * @param pClass
	 * @return
	 */
	public <T> T getService(Class<T> pClass) {
		return mServiceUtility.getService(pClass);
	}

	/**
	 * Delegation an die implementierende Klasse
	 * 
	 * @param pContext
	 *            der BundleContext
	 */
	public abstract void startDelegate(BundleContext pContext);

	/**
	 * Delegation an die implementierende Klasse
	 * 
	 * @param pContext
	 *            der BundleContext
	 */
	public abstract void stopDelegate(BundleContext pContext);

}
