package ch.jester.orm;

import java.sql.Connection;

import javax.persistence.EntityManagerFactory;

import org.osgi.framework.BundleContext;

import ch.jester.common.ui.activator.AbstractUIActivator;
import ch.jester.commonservices.api.logging.ILogger;
import ch.jester.orm.internal.ORMAutoDBHandler;

public class ORMPlugin extends AbstractUIActivator {
	// ExtensionPoint Name
	public final static String EP_CONFIGURATION = "Configuration";

	// Attribute des ExtensionPoints
	public final static String EP_CONFIGURATION_DATABASEMANAGERCLZ = "DatabaseManagerClass";
	public final static String EP_CONFIGURATION_ORMCONFIGURATION = "ORMConfiguration";

	// The shared instance.
	private static ORMPlugin mPlugin;

	private ILogger mLogger;

	private static ORMAutoDBHandler handler;

	public ORMPlugin() {
		mPlugin = this;
	}

	@Override
	public void startDelegate(BundleContext context) {
		handler = new ORMAutoDBHandler(this.getPreferenceStore(), this);
		mLogger = getActivationContext().getLogger();
		mLogger.info("ORMPlugin started");
		handler.initialize();
	}

	@Override
	public void stopDelegate(BundleContext context) {
		IDatabaseManager manager = handler.getDatabaseManager();
		if (manager != null) {
			manager.stop();
			manager.shutdown();

		}
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
		return handler.getORMConfiguration().getJPAEntityManagerFactory();
	}

	/**
	 * liefert die Configuration
	 * 
	 * @return
	 */
	public static IORMConfiguration getConfiguration() {
		return handler.getORMConfiguration();
	}

	/**
	 * liefert einen neue Connection
	 * @return
	 */
	public static Connection getConnection() {
		return handler.getORMConfiguration().getConnection();
	}

	/**
	 * liefert den IDataBaseManager oder null
	 * @return
	 */
	public IDatabaseManager getDataBaseManager() {
		return handler.getDatabaseManager();
	}

	public String getDataBaseTypeName() {
		return handler.getDataBaseTypeName();
	}
	
	
	

}
