package ch.jester.hibernate.helper;

import javax.persistence.EntityManagerFactory;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.hibernate.Session;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;

import ch.jester.common.activator.AbstractActivator;
import ch.jester.common.utility.ExtensionPointUtil;
import ch.jester.commonservices.api.logging.ILogger;

public class HibernatehelperPlugin extends AbstractActivator {
	//ExtensionPoint Name
	private String EP_CONFIGURATION = "Configuration";
	//Attribute des ExtensionPoints
	private final static String EP_CONFIGURATION_DATABASEMANAGERCLZ = "DatabaseManagerClass";
	private ILogger mLogger;
	private IDatabaseManager mDBManager;
	//The shared instance.
	private static HibernatehelperPlugin plugin;
	
	public HibernatehelperPlugin() {
		plugin = this;
	}

	@Override
	public void startDelegate(BundleContext context) {
		mLogger = getActivationContext().getLogger();
		mLogger.info("HibernatehelperPlugin started");
		startDB();

	}

	@Override
	public void stopDelegate(BundleContext context) {
		
		if(mDBManager!=null){	
			mDBManager.stop();
			mDBManager.shutdown();
		
		}
	}

	/**
	 * Returns the shared instance.
	 */
	public static HibernatehelperPlugin getDefault() {
		return plugin;
	}
	/**
	 * liefert die Id dieses Plugins aus dem Manifest
	 * @return
	 */
	public String getPluginId() {
		return getActivationContext().getPluginId();
	}
	
	public  static EntityManagerFactory getJPAEntitManagerFactor(){
		 return ConfigurationHelper.getJPAEntitManagerFactor();
	}
		/**
		 * liefert eine neue Session
		 * @return
		 */
	public	static synchronized Session getSession(){
			return ConfigurationHelper.getSession();
	}
	
	public IDatabaseManager getDataBaseManager(){
		return mDBManager;
	}
	
	private void startDB() {
		IConfigurationElement element = ExtensionPointUtil.getExtensionPointElement(getPluginId(),EP_CONFIGURATION);
		String dbmClassName = element.getAttribute(EP_CONFIGURATION_DATABASEMANAGERCLZ);
		if (dbmClassName != null) {
			try {
				Bundle databaseBundle = Platform.getBundle(element.getContributor().getName());
				getActivationContext().getLogger().info("DatabaseManagerClass is "+dbmClassName+" located in Bundle: "+databaseBundle);
				mDBManager =  (IDatabaseManager) element.createExecutableExtension(EP_CONFIGURATION_DATABASEMANAGERCLZ);
				mDBManager.start();
			} catch (CoreException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
	}
	}
}
