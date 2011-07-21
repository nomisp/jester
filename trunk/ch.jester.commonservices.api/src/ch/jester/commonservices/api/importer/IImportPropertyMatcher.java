package ch.jester.commonservices.api.importer;

import java.util.HashMap;

/**
 * Interface um ein Matching vom BeanProperty auf ein InputProperty zu machen
 *
 */
public interface IImportPropertyMatcher {
	/**
	 * Definiert die Property des Inputes
	 * @return ein String[] mit den Properties/Attributen
	 */
	public String[] getInputAttributes();
	/**Die Properties/Attribute der Klasse die erzeugt werden soll
	 * @return
	 */
	public String[] getDomainObjectProperties();
	/**
	 * Setzen der des Matchings vom Domain Property vs Input Property
	 * @param pMap
	 */
	public void setInputMatching(HashMap<String, String> pMap);
	/**
	 * Das Matching
	 * @return
	 */
	public HashMap<String, String> getInputMatching();
	/**
	 * In den Anfangszustand zurück
	 */
	public void resetInputMatching();
	/**
	 * 
	 * @return ein Translator
	 */
	public IPropertyTranslator getPropertyTranslator();
}
