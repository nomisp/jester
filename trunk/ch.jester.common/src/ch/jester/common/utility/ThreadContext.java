package ch.jester.common.utility;

/**
 * Utility Klasse um einfach den ThreadContext / Klassenloader zu wechseln
 *
 */
public class ThreadContext {
	ClassLoader oldL;
	/**
	 * speichert den aktuellen Classloader
	 */
	public void save(){
		oldL = Thread.currentThread().getContextClassLoader();
	}
	/**
	 * Setzt den übergebenen Classloader für den aktuellen Thread
	 * @param cl
	 */
	public void set(ClassLoader cl){
		save();
		Thread.currentThread().setContextClassLoader(cl);
	}
	/**
	 * Setzt den Classloader zurück
	 */
	public void restore(){
		Thread.currentThread().setContextClassLoader(oldL);
	}
}
