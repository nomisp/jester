package ch.jester.commonservices.api;

/**
 * Factory Interface für die Logger Erstellung
 *
 */
public interface ILoggerFactory {
	/**
	 * Gibt einen Logger für die übergebene Klasse zurück
	 * @param eine Klasse für den Loggernamen
	 * @return einen Logger
	 */
	public ILogger getLogger(Class<?> pClass);
	/**
	 * Debug ein- und Ausschalten
	 * @param debug ein-/ausschalten
	 */
	public void setDebug(boolean b);
	/**
	 * Ist Debug eingeschaltet?
	 * @return 
	 */
	public boolean isDebug();
}
