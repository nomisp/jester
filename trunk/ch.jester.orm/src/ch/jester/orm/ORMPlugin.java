package ch.jester.orm;

import java.sql.Connection;

import javax.persistence.EntityManagerFactory;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.action.StatusLineContributionItem;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;

import ch.jester.common.ui.activator.AbstractUIActivator;
import ch.jester.common.ui.services.IExtendedStatusLineManager;
import ch.jester.common.ui.utility.UIUtility;
import ch.jester.common.utility.ExtensionPointUtil;
import ch.jester.commonservices.api.logging.ILogger;
import ch.jester.orm.internal.ORMAutoDBHandler;
import ch.jester.orm.internal.ORMDBUtil;

public class ORMPlugin extends AbstractUIActivator {
	// ExtensionPoint Name
	private String EP_CONFIGURATION = "Configuration";

	// Attribute des ExtensionPoints
	private final static String EP_CONFIGURATION_DATABASEMANAGERCLZ = "DatabaseManagerClass";
	private final static String EP_CONFIGURATION_ORMCONFIGURATION = "ORMConfiguration";

	// The shared instance.
	private static ORMPlugin mPlugin;
	private static IORMConfiguration mConfig;
	private ILogger mLogger;
	private IDatabaseManager mDBManager;
	private ORMAutoDBHandler handler;
	private String dbtn;
	public ORMPlugin() {
		mPlugin = this;
	}

	@Override
	public void startDelegate(BundleContext context) {
		handler = new ORMAutoDBHandler(this.getPreferenceStore());
		mLogger = getActivationContext().getLogger();
		mLogger.info("ORMPlugin started");
		startDB();

	}

	@Override
	public void stopDelegate(BundleContext context) {

		if (mDBManager != null) {
			mDBManager.stop();
			mDBManager.shutdown();

		}
	}
	public String getDataBaseTypeName(){
		return dbtn;
	}

	/**
	 * Returns the shared instance.
	 */
	public static ORMPlugin getDefault() {
		return mPlugin;
	}

	/**
	 * liefert die Id dieses Plugins aus dem Manifest
	 * 
	 * @return
	 */
	public String getPluginId() {
		return getActivationContext().getPluginId();
	}

	public static EntityManagerFactory getJPAEntitManagerFactor() {
		return mConfig.getJPAEntityManagerFactory();
	}

	/**
	 * liefert die Configuration
	 * 
	 * @return
	 */
	public static IORMConfiguration getConfiguration() {
		return mConfig;
	}

	/**
	 * liefert einen neue Connection
	 * @return
	 */
	public static Connection getConnection() {
		return mConfig.getConnection();
	}

	/**
	 * liefert den IDataBaseManager oder null
	 * @return
	 */
	public IDatabaseManager getDataBaseManager() {
		return mDBManager;
	}
	
	private void startDB(){
		Bundle configuredDBBundle = handler.getDefaultDataBaseBundle();
		Bundle firstDBBundle = null;
		IConfigurationElement firstDBConfig = ExtensionPointUtil.getExtensionPointElement(getPluginId(), EP_CONFIGURATION);
		firstDBBundle = Platform.getBundle(firstDBConfig.getContributor().getName());
		
		IConfigurationElement usedElement = null;
		Bundle usedBundle = null;
		
		if(configuredDBBundle == null){
			 usedElement = firstDBConfig;
			 usedBundle = firstDBBundle;
		}else{
			IConfigurationElement[] elements =  ExtensionPointUtil.getExtensionPointElements(getPluginId(), EP_CONFIGURATION);
			for(IConfigurationElement c:elements){
				if(c.getContributor().getName()==configuredDBBundle.getSymbolicName()){
					usedElement = c;
					usedBundle = configuredDBBundle;
					break;
				}
			}
		}
		if(usedElement==null){
			usedElement = firstDBConfig;
		}
		this.dbtn = usedBundle.getHeaders().get("Bundle-Name").toString();
		
		dbConfig(usedElement);
	}

	private void dbConfig(IConfigurationElement element) {
		
		String dbmClassName = element
				.getAttribute(EP_CONFIGURATION_DATABASEMANAGERCLZ);

		element = ExtensionPointUtil.getExtensionPointElement(getPluginId(),
				EP_CONFIGURATION);
		String configClass = element.getAttribute(EP_CONFIGURATION_ORMCONFIGURATION);
		try {

			Bundle contributoBundle = Platform.getBundle(element
					.getContributor().getName());
			getActivationContext().getLogger().info(
					"ORMConfiguration is " + configClass
							+ " located in Bundle: " + contributoBundle);
			mConfig = (IORMConfiguration) element
					.createExecutableExtension(EP_CONFIGURATION_ORMCONFIGURATION);
			//nach hinten geschoben
			if (dbmClassName != null) {
				try {
					Bundle databaseBundle = Platform.getBundle(element
							.getContributor().getName());
					getActivationContext().getLogger().info(
							"DatabaseManagerClass is " + dbmClassName
									+ " located in Bundle: " + databaseBundle);
					mDBManager = (IDatabaseManager) element
							.createExecutableExtension(EP_CONFIGURATION_DATABASEMANAGERCLZ);
					mDBManager.start();
				} catch (CoreException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
