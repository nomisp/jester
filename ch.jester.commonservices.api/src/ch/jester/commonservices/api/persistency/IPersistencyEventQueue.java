package ch.jester.commonservices.api.persistency;



public interface IPersistencyEventQueue {
	public IPersistencyEvent getEvent() throws InterruptedException;
	public void addListener(IPersistencyListener listener);
	public void shutdown();
	public void dispatch(IPersistencyEvent pEvent);

}