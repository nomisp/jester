package ch.jester.commonservices.api.web;

/**
 * Hilfsservice um zu Testen, ob eine Internetverbindung besteht.
 *
 */
public interface IPingService {
	public final static int REACHABLE = 0;
	public final static int NOT_REACHABLE = -1;
	public final static int UNKNOWN = -99;
	/**Gibt Auskunft ob ein Server antwortet.
	 * @param pInetAddress
	 * @return
	 */
	public int ping(String pInetAddress);
	/**Startet einen konstanten Ping in einer Endlosschleife mit entsprechender Verzögerung.
	 * @param pInetAddress
	 * @param pReschedule
	 * @return
	 */
	public int ping(String pInetAddress, int pReschedule);
	
	/**
	 * Gibt es eine Internetconnection?
	 */
	public boolean isConnected();
}
