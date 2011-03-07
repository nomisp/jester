package ch.jester.hibernate.helper;

/**
 * Interface welches die verschiedenen DatabaseManager
 * der verschiedenen Datenbanken implementieren sollten.
 * Wird insbesondere zum starten von embedded DB's ben�tigt.
 * @author Peter
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
	
}
