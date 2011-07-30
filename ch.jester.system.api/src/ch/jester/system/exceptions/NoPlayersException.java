package ch.jester.system.exceptions;

/**
 * Exception welche geworfen werden kann
 * falls eine Kategorie die gepaart werden soll keine 
 * Spieler angehängt hat.
 * @author Peter
 *
 */
public class NoPlayersException extends Exception {
	private static final long serialVersionUID = -7287644168916712543L;

	public NoPlayersException() {
		super("The Category has no players attached.");
	}
	
	public NoPlayersException(String message) {
		super(message);
	}
}
