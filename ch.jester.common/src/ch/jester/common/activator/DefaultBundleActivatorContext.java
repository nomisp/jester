package ch.jester.common.activator;

import org.osgi.framework.BundleContext;

import ch.jester.common.utility.CallerUtility;

import ch.jester.commonservices.api.bundle.IActivationContext;
import ch.jester.commonservices.api.logging.ILogger;
import ch.jester.commonservices.api.logging.ILoggerFactory;
import ch.jester.commonservices.util.ServiceUtility;

/**
 * Die Defaultimplementierung von IActivationContext. Die Informationen werden vom BundleContext zur Verfügung gestellt
 */
public class DefaultBundleActivatorContext<V> implements IActivationContext<V> {
	protected BundleContext mContext;
	/**
	 * @uml.property  name="mServiceUtility"
	 * @uml.associationEnd  
	 */
	private ServiceUtility mServiceUtility;
	private String mPluginId;
	private V mActivator;
	public DefaultBundleActivatorContext(BundleContext pContext, V pActivator){
		mContext=pContext;
		mServiceUtility = new ServiceUtility(mContext);
		mPluginId=pContext.getBundle().getSymbolicName();
	}
	
	@Override
	public String getPluginId(){
		return mPluginId;
	}

	@Override
	public BundleContext getBundleContext() {
		return mContext;
	}

	@Override
	public ServiceUtility getServiceUtil() {
		return mServiceUtility;
	}

	@Override
	public ILogger getLogger(Class<?> t) {
		ILoggerFactory loggerFactory = null;
		loggerFactory = getService(ILoggerFactory.class);
		if(loggerFactory==null){
			System.out.println("No Logger Factory: eiter no Factory is installed, or the service has already been deactivated");
			return null;
		}
		return loggerFactory.getLogger(t);
	}

	@Override
	public ILogger getLogger() {
		String callingClassName = CallerUtility.getCaller(2).getCallerClass();
		Class<?> caller;
		try {
			caller = mContext.getBundle().loadClass(callingClassName);
			return getLogger(caller);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public <T> T getService(Class<T> pClass) {
		return mServiceUtility.getService(pClass);
	}

	@Override
	public V getActivator() {
		return mActivator;
	}
}
