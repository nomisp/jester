package ch.jester.system.api.pairing;

import ch.jester.commonservices.api.components.IEPEntry;

public interface IPairingAlgorithmEntry extends IEPEntry<IPairingAlgorithm> {
	public final static String SHORTTYPE = "shortType";
	public final static String TYPEDESCRIPTION = "typeDescription";
	/**
	 * @return einen String 
	 */
	public String getShortType();
	/**
	 * @return eine detailliertere Beschreibung
	 */
	public String getDescription();
}
