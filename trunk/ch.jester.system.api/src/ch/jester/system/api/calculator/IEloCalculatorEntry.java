package ch.jester.system.api.calculator;

import ch.jester.commonservices.api.components.IEPEntry;

public interface IEloCalculatorEntry extends IEPEntry<IEloCalculator> {
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
