package ch.jester.commonservices.api.logging;

/**
 * Factory Interface für die Logger Erstellung
 */
public interface ILoggerFactory {
	/**
	 * Gibt einen Logger für die übergebene Klasse zurück
	 * @param eine Klasse für den Loggernamen
	 * @return einen Logger
	 */
	public ILogger getLogger(Class<?> pClass);
	/**
	 * Debug ein- und ausschalten
	 * @param debug   ein- oder ausschalten
	 * @uml.property  name="debug"
	 */
	public void setDebug(boolean b);
	/**
	 * Ist Debug eingeschaltet?
	 * @return
	 * @uml.property  name="debug"
	 */
	public boolean isDebug();
}
