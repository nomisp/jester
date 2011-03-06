package ch.jester.hibernate.helper;

import java.util.List;

import org.eclipse.ui.plugin.*;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IContributor;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.resource.ImageDescriptor;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;

public class HibernatehelperPlugin extends AbstractUIPlugin {
	
	//The shared instance.
	private static HibernatehelperPlugin plugin;
	/**
	 * der Kontext dieses Plugins
	 */
	private static BundleContext context;
	
	/**
	 * The constructor.
	 */
	public HibernatehelperPlugin() {
		plugin = this;
	}

	/**
	 * This method is called upon plug-in activation
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		HibernatehelperPlugin.context=context;
		System.out.println("HibernateHelper started");
		startHSQLDB();
	}

	/**
	 * This method is called when the plug-in is stopped
	 */
	public void stop(BundleContext context) throws Exception {
		super.stop(context);
		plugin = null;
	}

	/**
	 * Returns the shared instance.
	 */
	public static HibernatehelperPlugin getDefault() {
		return plugin;
	}

	/**
	 * Returns an image descriptor for the image file at the given
	 * plug-in relative path.
	 *
	 * @param path the path
	 * @return the image descriptor
	 */
	public static ImageDescriptor getImageDescriptor(String path) {
		return AbstractUIPlugin.imageDescriptorFromPlugin("ch.jester.hibernate.helper", path);
	}

	public static BundleContext getContext() {
		return context;
	}
	
	/**
	 * liefert das ConfigurationElment, das für den angegebenen ExtensionPoint mit
	 * dem angegebenen Namen deklariet ist.
	 * Wenn kein Element gefunden werden kann, wird null geliefert
	 * Der Extensionpoint muß von diesem Plugin deklariert worden sein
	 * und ein anderes Plugin hängt sich dort ein und setzt den konkreten Wert
	 * Dessen Element wird dann geliefert 
	 * @param extensionPointId
	 * @return
	 */
	public static IConfigurationElement getExtensionPointElement(String extensionPointId) {
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
	public static String getId() {
		return getDefault().getBundle().getSymbolicName();
	}
	
	private void startHSQLDB() {
		IConfigurationElement element = HibernatehelperPlugin.getExtensionPointElement("Configuration");
		String dbmClassName = element.getAttribute("DatabaseManagerClass");
		IContributor contributor = element.getContributor();
		Bundle databaseBundle = Platform.getBundle(contributor.getName());
		if (dbmClassName != null) {
			try {
				Class<IDatabaseManager> c = databaseBundle.loadClass(dbmClassName);
				IDatabaseManager dbmanager = (IDatabaseManager) c.newInstance();
				dbmanager.start();
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
