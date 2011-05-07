package ch.jester.commonservices.api.persistency;

public interface IPersistencyFilter {
	public boolean dispatch(IPersistencyEvent pEvent);
}
