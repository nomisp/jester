package ch.jester.system.api.ranking;

import ch.jester.commonservices.api.components.IEPEntry;

/**
 * Kapselt die ExtensionPoint-Definition
 *
 */
public interface IRankingSystemEntry extends IEPEntry<IRankingSystem> {
	public static final String PLUGINID = "pluginId";
	public static final String CLASS = "class";
	public static final String SHORTTYPE = "shortType";
	public static final String TYPEDESCRIPTION = "typeDescription";

	/**
	 * Liefert die Plugin-Id welches den PairingAlgorithmEntry beinhaltet
	 * @return	Plugin-ID
	 */
	public String getPluginId();
	
	/**
	 * Klasse der Implementierung
	 * @return	Implementierungsklasse
	 */
	public String getImplementationClass();
	/**
	 * Kurzbeschreibung
	 * @return einen String
	 */
	public String getShortType();
	/**
	 * Beschreibung
	 * @return eine detailliertere Beschreibung
	 */
	public String getDescription();
}
