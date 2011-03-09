package ch.jester.commonservices.api.importer;

/** Beschreibt ein Eintrag in einer Liste
 *  (im GUI)
 */
public interface IImportHandlerEntry {
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
	/**Holt den BackEnd Service
	 * @return den Service
	 */
	public IImportHandler getService();
}
