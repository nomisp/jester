package ch.jester.common.utility;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;

public class ExtensionPointUtil {
	/**
	 * liefert das ConfigurationElment, das für den angegebenen ExtensionPoint mit
	 * dem angegebenen Namen deklariet ist.
	 * Wenn kein Element gefunden werden kann, wird null geliefert
	 * Der Extensionpoint muss von diesem Plugin deklariert worden sein
	 * und ein anderes Plugin hängt sich dort ein und setzt den konkreten Wert
	 * Dessen Element wird dann geliefert 
	 * @param pExtensionPointId
	 * @return
	 */
	public static IConfigurationElement getExtensionPointElement(String pPluginId, String pExtensionPointId) {
		// die Id des RCP-Plugins ermitteln
		IExtensionRegistry registry = Platform.getExtensionRegistry();
		IConfigurationElement[] elements = registry.getConfigurationElementsFor(pPluginId, pExtensionPointId);	
		for (IConfigurationElement element : elements) {
			// Element mit dem angegebenen Namen liefern
			String name = element.getName();			
			if (name.equals(pExtensionPointId)) {				
				return element;
			}
		}
		// nix gefunden, null liefern
		return null;
	}
	public static IConfigurationElement[] getExtensionPointElements(String pPluginId, String pExtensionPointId) {
		// die Id des RCP-Plugins ermitteln
		IExtensionRegistry registry = Platform.getExtensionRegistry();
		IConfigurationElement[] elements = registry.getConfigurationElementsFor(pPluginId, pExtensionPointId);	
		return elements;
	}
	
}
