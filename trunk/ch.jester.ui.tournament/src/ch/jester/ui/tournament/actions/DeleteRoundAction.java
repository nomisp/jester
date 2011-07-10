package ch.jester.ui.tournament.actions;

import org.eclipse.jface.action.Action;

import ch.jester.model.Round;
import ch.jester.ui.tournament.forms.CategoryMasterDetail;
import ch.jester.ui.tournament.nl1.Messages;

/**
 * Action zum löschen von Runden
 * @author Peter
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
		round.getCategory().removeRound(round);
		categoryMasterDetail.refresh();
		categoryMasterDetail.setEditorDirty();
	}
}
