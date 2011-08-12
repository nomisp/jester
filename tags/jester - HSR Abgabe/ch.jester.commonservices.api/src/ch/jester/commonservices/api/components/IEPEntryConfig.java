package ch.jester.commonservices.api.components;

/**
 * Ein Configuration Eintrag.
 * Kann implementiert werden, wenn Zugriff auf die ExtensionPoint deklarationen nötig sind.
 *
 */
public interface IEPEntryConfig {
	/**
	 * Setzt den Entry
	 * @param e
	 */
	public void setEPEntry(IEPEntry<?> e);
}
