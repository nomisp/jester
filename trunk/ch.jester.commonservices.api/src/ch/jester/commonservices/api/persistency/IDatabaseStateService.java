package ch.jester.commonservices.api.persistency;

/**
 * Der DatabaseStateService führt ein Runnable aus, wenn der State wechselt.
 */
public interface IDatabaseStateService {
	public enum State{

		INIT, 
		RUN;
	}
	/**Sofern der interne State bereits im übergebenen State ist, wird das Runnable
	 * im selben Job/Thread direkt ausgeführt. Ansonsten wird es gecached, 
	 * bis der entsprechende State eintritt und dann ausgeführt. Dies kann von 
	 * einem separaten Job erfolgen.
	 * @param newState
	 * @param r
	 */
	public void executeOnStateChange(State newState, Runnable r);
	/**
	 * Gibt den internen State zurück
	 * @return
	 */
	public State getState();
	/**
	 * Setzt den State. Die Implementation hat dafür zu sorgen, dass zwischengespeicherte Runnables mit entsprechendem State ausgeführt werden
	 * @param  pState
	 */
	public void setState(State pState);
}
