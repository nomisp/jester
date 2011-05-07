package ch.jester.commonservices.api.persistency;



public interface IPersistencyListener extends IPersistencyFilter{
	void setFilter(IPersistencyFilter pFilter);
	void persistencyEvent(IPersistencyEvent pEvent);

}
