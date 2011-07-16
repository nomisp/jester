package ch.jester.ui.tournament.cnf;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.navigator.CommonNavigator;
import org.eclipse.ui.navigator.CommonViewer;

import ch.jester.model.Root;

/**
 * Damit beim Aufstarten ein Dummy-Root-Objekt für den CommonNavigator als initialInput
 * zur Verfügung gestellt werden kann wird diese abgeleitete Klasse benötigt.
 * 
 *
 */
public class TournamentNavigator extends CommonNavigator {

	public static final String ID = "ch.jester.ui.tournament.cnf.view";
	Composite parent;
	CommonViewer viewer;
	public TournamentNavigator() {
		
	}

	protected IAdaptable getInitialInput() {
		return new Root();
	}
}
