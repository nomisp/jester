package ch.jester.commonservices.api.persistency;


/**
 * Interface welches die verschiedenen DatabaseManager
 * der verschiedenen Datenbanken implementieren sollten.
 * Wird insbesondere zum starten von embedded DB's benötigt.
 *
 */
public interface IDatabaseManager {

	/**
	 * Startet die Datenbank
	 */
	public void start();
	
	/**
	 * Stoppt die Datenbank
	 */
	public void stop();
	
	/**
	 * Sicheres runterfahren des DB-Servers
	 */
	public void shutdown();
	
	/**Injected die Configuration um diese zu editieren/speichern
	 * @param pConfig
	 */
	public void editORMConfiguration(IORMConfiguration pConfig);
	
	
}
