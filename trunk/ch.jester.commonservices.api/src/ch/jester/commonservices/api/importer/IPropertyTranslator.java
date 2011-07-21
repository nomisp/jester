package ch.jester.commonservices.api.importer;

/**
 * Übersetzungs Interface für Object Properties
 *
 */
public interface IPropertyTranslator {
	/**
	 * Gibt das Objekt Property für dieses übersetzte Property zurück
	 * @param translatedProperty
	 * @return
	 */
	public String getProperty(String translatedProperty);
	/**
	 * Übersetzt das Objekt Property
	 * @param property
	 * @return
	 */
	public String getTranslation(String property);
	/**
	 * Alle Übersetzungen
	 * @return
	 */
	public String[] getTranslatedProperties();
}
