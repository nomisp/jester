package ch.jester.ui.tournament.ctrl;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.beans.BeansObservables;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.swt.SWT;

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
		IObservableValue nameObserveWidget = SWTObservables.observeText(tournamentDetails.getNameText(), SWT.Modify);
		IObservableValue nameObserveValue = BeansObservables.observeValue(tournament, "name");
		bindingContext.bindValue(nameObserveWidget, nameObserveValue, new UpdateValueStrategy(UpdateValueStrategy.POLICY_ON_REQUEST), null);
		
		IObservableValue descriptionObserveWidget = SWTObservables.observeText(tournamentDetails.getDescriptionText(), SWT.Modify);
		IObservableValue descriptionObserveValue = BeansObservables.observeValue(tournament, "description");
		bindingContext.bindValue(descriptionObserveWidget, descriptionObserveValue, new UpdateValueStrategy(UpdateValueStrategy.POLICY_ON_REQUEST), null);
		
		IObservableValue dateFromWidget = SWTObservables.observeSelection(tournamentDetails.getDateFrom());
		IObservableValue dateFromValue = BeansObservables.observeValue(tournament, "dateFrom");
		bindingContext.bindValue(dateFromWidget, dateFromValue, new UpdateValueStrategy(UpdateValueStrategy.POLICY_ON_REQUEST), null);
		
		IObservableValue dateToWidget = SWTObservables.observeSelection(tournamentDetails.getDateTo());
		IObservableValue dateToValue = BeansObservables.observeValue(tournament, "dateTo");
		bindingContext.bindValue(dateToWidget, dateToValue, new UpdateValueStrategy(UpdateValueStrategy.POLICY_ON_REQUEST), null);
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
