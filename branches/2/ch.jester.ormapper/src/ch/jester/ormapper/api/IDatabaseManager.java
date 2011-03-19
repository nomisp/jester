package ch.jester.ormapper.api;




/**
 * Interface welches die verschiedenen DatabaseManager
 * der verschiedenen Datenbanken implementieren sollten.
 * Wird insbesondere zum starten von embedded DB's benötigt.
 *
 */
public interface IDatabaseManager {
	
	public void setIDataBaseDefinition(IDatabaseDefinition pDefinition);
	
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
	
	public String getIP();
	public String getConnectionurl();

	public void getDriver();	
}
