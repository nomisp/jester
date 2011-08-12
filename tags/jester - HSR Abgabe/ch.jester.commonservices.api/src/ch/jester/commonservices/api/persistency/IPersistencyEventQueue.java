package ch.jester.commonservices.api.persistency;



/**
 * Eine EventQueue um DB Events weiterzuleiten
 *
 */
public interface IPersistencyEventQueue {
	/**Methode die auf den nächsten Event wartet und diesen zurückgibt, sobald verfügbar
	 * @return
	 * @throws InterruptedException
	 */
	public IPersistencyEvent getEvent() throws InterruptedException;
	/**
	 * Hinzufügen eines Listeners
	 * @param listener
	 */
	public void addListener(IPersistencyListener listener);
	/**
	 * Beendet das horchen auf Events
	 */
	public void shutdown();
	/**Alle Listener informieren
	 * @param pEvent
	 */
	public void dispatch(IPersistencyEvent pEvent);
	
	public void removeListener(IPersistencyListener queueListener);

	public int size();
}