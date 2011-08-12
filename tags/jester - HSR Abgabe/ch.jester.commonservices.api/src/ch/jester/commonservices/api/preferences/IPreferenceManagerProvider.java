package ch.jester.commonservices.api.preferences;

/**
 * Mit diesem Interface können PreferenceManagers am {@link IPreferenceRegistration} Service
 * registriert werden
 *
 */
public interface IPreferenceManagerProvider {
	/**Bietet Zugriff auf den PreferenceManager
	 * Die Implemenation soll dafür sorgen, dass der PreferenceManager nur zurückgegeben wird, wenn 
	 * <code>pKey.equals(aPreferenceManager.getId()) == true<code>
	 * @param pKey
	 * @return
	 */
	public IPreferenceManager getPreferenceManager(String pKey);
}
