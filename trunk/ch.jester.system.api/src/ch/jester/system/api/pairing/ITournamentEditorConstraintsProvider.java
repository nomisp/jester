package ch.jester.system.api.pairing;

import ch.jester.system.api.pairing.ui.TournamentEditorConstraints;

/**
 * Schnittstelle für die Turnier-Editor Constraints
 * welche Paarungssysteme welche solche setzen wollen implementieren.
 *
 */
public interface ITournamentEditorConstraintsProvider {
	
	/**
	 * Liefert die Turnier-Editor Constraints
	 * @return
	 */
	public TournamentEditorConstraints getTournamentEditorConstraints();
}
