package ch.jester.system.api.pairing;

import ch.jester.commonservices.api.components.IEPEntry;

/**
 * Kapselt die ExtensionPoint-Definition
 *
 */
public interface IPairingAlgorithmEntry extends IEPEntry<IPairingAlgorithm> {
	public static final String PLUGINID = "pluginId";
	public static final String CLASS = "class";
	public static final String SHORTTYPE = "shortType";
	public static final String TYPEDESCRIPTION = "typeDescription";
	public static final String SETTINGSPAGE = "settingsPage";
	
	/**
	 * Liefert die Plugin-Id welches den PairingAlgorithmEntry beinhaltet
	 * @return
	 */
	public String getPluginId();
	/**
	 * Klasse der Implementierung
	 * @return
	 */
	public String getImplementationClass();
	/**
	 * @return einen String 
	 */
	public String getShortType();
	/**
	 * @return eine detailliertere Beschreibung
	 */
	public String getDescription();
	
	/**
	 * Liefert die Klasse der FormPage für Pluginspezifische Einstellungen
	 * @return	SettingsPage-Klasse
	 */
	public String getSettingsPage();
}
