package ch.jester.system.exceptions;

/**
 * Exception, welche geworfen werden kann,
 * wenn noch nicht alle Resultate einer Runde erfasst wurden.
 * @author Peter
 *
 */
public class NotAllResultsException extends Exception {
	private static final long serialVersionUID = 3116104907622904576L;

	public NotAllResultsException() {
		super("There are results missing for further pairings!");
	}
	
	public NotAllResultsException(String message) {
		super(message);
	}
}
