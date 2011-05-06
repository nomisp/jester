package ch.jester.commonservices.api.persistencyevent;

public interface IPersistencyFilter {
	public boolean dispatch(PersistencyEvent pEvent);
}
