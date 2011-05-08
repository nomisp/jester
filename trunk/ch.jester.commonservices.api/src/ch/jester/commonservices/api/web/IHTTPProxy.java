package ch.jester.commonservices.api.web;


import java.net.Proxy;

/**
 * Service Interface für das erzeugen eines Proxys.
 * (Systemweit)
 */
public interface IHTTPProxy {
	/**Erstellt einen Proxy
	 * @param pProxyAdress
	 * @param pProxyPort
	 */
	public void createHTTPProxy(String pProxyAdress, int pProxyPort);
	/**
	 * löscht den Proxy
	 */
	public void deleteProxy();
	/**Gibt den aktuell gesetzten Proxy zurück
	 * @return
	 */
	public Proxy getHTTPProxy();
	/**gibt die Adresse des aktuellen Proxys zürück
	 * @return
	 */
	public String getAddress();
	/**gibt den Port des aktuellen Proxys zürück
	 * @return
	 */
	public int getPort();
}
