package ch.jester.commonservices.api.components;

/**
 * Hilfsklasse die einen Service kapselt bzw dessen Extension Point Definition
 *
 * @param <T> der SerivceTyp
 */
public interface IEPEntry<T> {
	
	/**
	 * @param pPropertyKey
	 * @return Wert für Key, oder null
	 */
	public String getProperty(String pPropertyKey);

	/** gibt den Service zurück
	 * @return
	 */
	public abstract T getService(); 
}