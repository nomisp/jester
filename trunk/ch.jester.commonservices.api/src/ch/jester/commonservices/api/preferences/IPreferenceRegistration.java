package ch.jester.commonservices.api.preferences;


/**
 * Service Interface das eine loose Koppelung zwischen dem Aufrufer (z.B einer IDPreference Page)
 * und einem Provider für den IPreferenceManager ermöglicht
 *
 */
public interface IPreferenceRegistration {
	
	/**
	 * Ristriert einen Provider am Service unter der übergebenen ID
	 * @param pId
	 * @param pProvider
	 */
	public void registerPreferenceProvider(String pId, IPreferenceManagerProvider pProvider);
	/**
	 * Registriert einen Provider am Service.
	 * 
	 * @param pId
	 * @param pProvider
	 */
	public void registerPreferenceProvider(IPreferenceManagerProvider pProvider);
	/**
	 * Entfernen eines Providers
	 * @param pId
	 */
	public void unregisterPreferenceProvider(String pId);
	/**
	 * Entfernen eines Providers
	 * @param pProvider
	 */
	public void unregisterPreferenceProvider(IPreferenceManagerProvider pProvider);

	/**
	 * Versucht einen Provider zu finden.
	 * Wurde ein Anonymer Provider übergeben {@link this#registerPreferenceProvider(IPreferenceManagerProvider)}
	 * <br> - so wird die ID des {@link IPreferenceManager} verglichen.
	 * <br> - sonst wird die mit der übergeben id von {@link this#registerPreferenceProvider(String, IPreferenceManagerProvider)} gesucht.
	 * @param pId
	 * @return den IPreferenceProvider oder null, wenn keiner gefunden worden ist
	 */
	public IPreferenceManagerProvider findProvider(String pId);
	
	/**
	 * Erzeugt einen neuen Manager
	 * @return einen neuen IPreferenceManager
	 */
	public IPreferenceManager createManager();
}
