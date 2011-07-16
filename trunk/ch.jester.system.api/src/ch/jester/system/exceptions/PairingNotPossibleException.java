package ch.jester.system.exceptions;

/**
 * Exception welche geworfen werden kann, wenn die Paarungsauslosung nicht durchgeführt werden kann.
 * Die ist z.B. der Fall wenn weder Runden noch Spieler einer Kategorie zugewisen wurden.
 *
 */
public class PairingNotPossibleException extends Exception {
	private static final long serialVersionUID = 6528027385369255243L;
	
	public PairingNotPossibleException() {
		super("Pairing is not possible! The Category must have Players and Rounds associated");
	}
	
	public PairingNotPossibleException(String message) {
		super(message);
	}
}
