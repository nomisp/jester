package ch.jester.ui.tournament.actions;

import messages.Messages;

import org.eclipse.jface.action.Action;

import ch.jester.model.Category;
import ch.jester.model.Round;
import ch.jester.ui.tournament.forms.CategoryMasterDetail;

/**
 * Action zum Löschen von Runden
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
		categoryMasterDetail.removeRound(category, round);
	
	}
}
