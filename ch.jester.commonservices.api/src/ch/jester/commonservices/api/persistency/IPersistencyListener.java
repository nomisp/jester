package ch.jester.commonservices.api.persistency;



/**
 * Das Listener Interface
 *
 */
public interface IPersistencyListener extends IPersistencyFilter{
	/**Setzt den nächsten Filter
	 * @param pFilter
	 */
	void setFilter(IPersistencyFilter pFilter);
	/**Der Event, der soeben passiert ist.
	 * @param pEvent
	 */
	void persistencyEvent(IPersistencyEvent pEvent);

}
