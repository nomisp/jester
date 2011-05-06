package ch.jester.commonservices.api.persistencyevent;



public interface IPersistencyListener extends IPersistencyFilter{
	void setFilter(IPersistencyFilter pFilter);
	void persistencyEvent(PersistencyEvent pEvent);

}
