package ch.jester.commonservices.api.persistency;

/**
 * Der Filter kann dazu verwendet werden, um der IPersistencyEventQueue
 * mitzuteilen, ob er überhaupt informiert werden möchte.
 *
 */
public interface IPersistencyFilter {
	public boolean dispatch(IPersistencyEvent pEvent);
}
