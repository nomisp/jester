package ch.jester.system.exceptions;

/**
 * Exception welche geworfen werden kann, wenn 
 * z.B. ein Turnier abgeschlossen wurde und nochmals versucht wird Paarungen auszulosen.
 * @author Peter
 *
 */
public class TournamentFinishedException extends Exception {
	private static final long serialVersionUID = 3953050735185803022L;

	public TournamentFinishedException() {
		super("The tournament has already been finished");
	}
	
	public TournamentFinishedException(String message) {
		super(message);
	}
}
