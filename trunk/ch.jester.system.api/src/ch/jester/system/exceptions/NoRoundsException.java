package ch.jester.system.exceptions;

/**
 * Exception welche geworfen werden kann
 * falls eine Kategorie die gepaart werden soll keine 
 * Runden angehängt hat.
 *
 */
public class NoRoundsException extends Exception {
	private static final long serialVersionUID = -7287644168916712543L;

	public NoRoundsException() {
		super("The Category has no rounds attached.");
	}
	
	public NoRoundsException(String message) {
		super(message);
	}
}
