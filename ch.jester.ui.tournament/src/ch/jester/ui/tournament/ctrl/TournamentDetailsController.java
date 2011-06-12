package ch.jester.ui.tournament.ctrl;

import org.eclipse.core.databinding.DataBindingContext;

import ch.jester.common.ui.editorutilities.DirtyManager;
import ch.jester.model.Tournament;
import ch.jester.ui.tournament.forms.TournamentFormPage;

public class TournamentDetailsController {
	private TournamentFormPage tournamentDetails;
	private DirtyManager dirtyManager;
	private Tournament tournament;
	private DataBindingContext bindingContext;

	public TournamentDetailsController(TournamentFormPage tournamentDetails) {
		this.tournamentDetails = tournamentDetails;
		dirtyManager = tournamentDetails.getDirtyManager();
		if (tournament != null) {
			bindingContext = initDataBindings();
		}
	}

	protected DataBindingContext initDataBindings() {
		DataBindingContext bindingContext = new DataBindingContext();
		// TODO Peter: Bindings!
		return bindingContext;
	}

	public DirtyManager getDirtyManager() {
		return dirtyManager;
	}

	public void updateModel(){
		bindingContext.updateModels();
	}
	public void updateUI(){
		bindingContext.updateTargets();
	}

	public Tournament getTournament() {
		return tournament;
	}

	public void setTournament(Tournament tournament) {
		setTournament(tournament, true);
	}
	
	public void setTournament(Tournament tournament, boolean update) {
		this.tournament = tournament;
		if (update) {
			if (bindingContext != null) {
				bindingContext.dispose();
				bindingContext = null;
			}
			if (tournament != null) {
				bindingContext = initDataBindings();
			}
		}
	}
}
