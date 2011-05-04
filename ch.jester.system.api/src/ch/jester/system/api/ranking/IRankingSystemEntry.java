package ch.jester.system.api.ranking;

import ch.jester.commonservices.api.components.IEPEntry;

public interface IRankingSystemEntry extends IEPEntry<IRankingSystem> {
	public static final String CLASS = "class";
	public static final String SHORTTYPE = "shortType";
	public static final String TYPEDESCRIPTION = "typeDescription";
	
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
}
