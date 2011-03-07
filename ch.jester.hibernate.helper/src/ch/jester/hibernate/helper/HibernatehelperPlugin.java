package ch.jester.hibernate.helper;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IContributor;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;

import ch.jester.common.activator.AbstractActivator;
import ch.jester.commonservices.api.logging.ILogger;

public class HibernatehelperPlugin extends AbstractActivator {
	ILogger logger;
	IDatabaseManager mDBManager;
	//The shared instance.
	private static HibernatehelperPlugin plugin;
	
	/**
	 * The constructor.
	 */
	public HibernatehelperPlugin() {
		plugin = this;
	}

	/**
	 * This method is called upon plug-in activation
	 */
	public void startDelegate(BundleContext context) {
		logger = getActivationContext().getLogger();
		logger.info("HibernatehelperPlugin started");
		startHSQLDB();
	}

	/**
	 * This method is called when the plug-in is stopped
	 */
	public void stopDelegate(BundleContext context) {
		if(mDBManager!=null){
			mDBManager.stop();
		}
	}

	/**
	 * Returns the shared instance.
	 */
	public static HibernatehelperPlugin getDefault() {
		return plugin;
	}
	
	/**
	 * liefert das ConfigurationElment, das f�r den angegebenen ExtensionPoint mit
	 * dem angegebenen Namen deklariet ist.
	 * Wenn kein Element gefunden werden kann, wird null geliefert
	 * Der Extensionpoint mu� von diesem Plugin deklariert worden sein
	 * und ein anderes Plugin h�ngt sich dort ein und setzt den konkreten Wert
	 * Dessen Element wird dann geliefert 
	 * @param extensionPointId
	 * @return
	 */
	public  IConfigurationElement getExtensionPointElement(String extensionPointId) {
		// die Id des RCP-Plugins ermitteln
		String pluginid = getId();
		IExtensionRegistry registry = Platform.getExtensionRegistry();
		IConfigurationElement[] elements = registry.getConfigurationElementsFor(pluginid, extensionPointId);	
		for (IConfigurationElement element : elements) {
			// Element mit dem angegebenen Namen liefern
			String name = element.getName();			
			if (name.equals(extensionPointId)) {				
				return element;
			}
		}
		// nix gefunden, null liefern
		return null;
	}
	
	/**
	 * liefert die Id dieses Plugins aus dem Manifest
	 * @return
	 */
	public String getId() {
		return getActivationContext().getPluginId();
	}
	
	private void startHSQLDB() {
		
		IConfigurationElement element = getExtensionPointElement("Configuration");
		String dbmClassName = element.getAttribute("DatabaseManagerClass");
		
		IContributor contributor = element.getContributor();
		Bundle databaseBundle = Platform.getBundle(contributor.getName());
		getActivationContext().getLogger().info("DatabaseManagerClass is "+dbmClassName+" located in Bundle: "+databaseBundle);
		if (dbmClassName != null) {
			try {
				Class<IDatabaseManager> c = databaseBundle.loadClass(dbmClassName);
				mDBManager = (IDatabaseManager) c.newInstance();
				mDBManager.start();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
	}

}
