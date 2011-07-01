package ch.jester.ui.tournament.actions;

import org.eclipse.jface.action.Action;

import ch.jester.model.Round;

/**
 * Action zum löschen von Runden
 * @author Peter
 *
 */
public class DeleteRoundAction extends Action {
	
	private Round round;
	
	public DeleteRoundAction(Round round) {
		this.round = round;
	}
	
	@Override
	public String getText() {
		return "DeleteRound";
	}

	@Override
	public void run() {
		round.getCategory().removeRound(round);
	}
}
