package ch.jester.ui.tournament.cnf;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.ui.navigator.CommonNavigator;

import ch.jester.model.Root;

/**
 * Damit beim Aufstarten ein Dummy-Root-Objekt für den CommonNavigator als initialInput
 * zur Verfügung gestellt werden kann wird diese abgeleitete Klasse benötigt.
 * 
 *
 */
public class TournamentNavigator extends CommonNavigator {

	public static final String ID = "ch.jester.ui.tournament.cnf.view";
	
	public TournamentNavigator() {
		
	}

	protected IAdaptable getInitialInput() {
		return new Root();
	}
}
