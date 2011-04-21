package ch.jester.commonservices.api.persistencyevent;


public interface IPersistencyEventQueue {
	public PersistencyEvent getEvent() throws InterruptedException;
	public void addListener(IPersistencyListener listener);
	public void shutdown();
	public void dispatch(PersistencyEvent pEvent);

}