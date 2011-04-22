package ch.jester.system.api.ranking;

import ch.jester.commonservices.api.components.IEPEntry;

public interface IRankingSystemEntry extends IEPEntry<IRankingSystem> {
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
