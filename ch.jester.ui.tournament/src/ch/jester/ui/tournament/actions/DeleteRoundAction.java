package ch.jester.ui.tournament.actions;

import java.util.List;

import messages.Messages;

import org.eclipse.jface.action.Action;

import ch.jester.model.Category;
import ch.jester.model.Round;
import ch.jester.ui.tournament.forms.CategoryMasterDetail;

/**
 * Action zum löschen von Runden
 *
 */
public class DeleteRoundAction extends Action {
	
	private Round round;
	private CategoryMasterDetail categoryMasterDetail;
	
	public DeleteRoundAction(Round round, CategoryMasterDetail categoryMDBlock) {
		this.round = round;
		this.categoryMasterDetail = categoryMDBlock;
	}
	
	@Override
	public String getText() {
		return Messages.Action_DeleteRound_text;
	}

	@Override
	public void run() {
		Category category = round.getCategory();
		category.removeRound(round);
		List<Round> rounds = category.getRounds();
		if (rounds.size() > round.getNumber()) {	// Eine Runde zwischen drin wurde gelöscht. Nummern nachführen.
			for (int i = round.getNumber()-1; i < rounds.size(); i++) {
				rounds.get(i).setNumber(i+1);
			}
		}
		categoryMasterDetail.refresh();
		categoryMasterDetail.setEditorDirty();
	}
}
