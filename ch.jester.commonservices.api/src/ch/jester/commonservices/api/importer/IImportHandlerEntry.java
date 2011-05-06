package ch.jester.commonservices.api.importer;

import ch.jester.commonservices.api.components.IEPEntry;

/** Beschreibt ein Eintrag in einer Liste
 *  (im GUI)
 */
@SuppressWarnings("rawtypes")
public interface IImportHandlerEntry extends IEPEntry<IImportHandler>{
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
