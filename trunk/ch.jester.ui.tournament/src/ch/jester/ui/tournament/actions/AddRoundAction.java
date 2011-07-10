package ch.jester.ui.tournament.actions;

import org.eclipse.jface.action.Action;

import ch.jester.model.Category;
import ch.jester.model.Round;
import ch.jester.model.factories.ModelFactory;
import ch.jester.ui.tournament.forms.CategoryMasterDetail;
import ch.jester.ui.tournament.nl1.Messages;

/**
 * Action um Runden zu einem Turnier hinzuzufügen
 * @author Peter
 *
 */
public class AddRoundAction extends Action {
	private Category category;
	private CategoryMasterDetail categoryMasterDetail;
	
	public AddRoundAction(Category cat, CategoryMasterDetail categoryMDBlock) {
		this.category = cat;
		this.categoryMasterDetail = categoryMDBlock;
	}
	
	@Override
	public String getText() {
		return Messages.Action_AddRound_text;
	}

	@Override
	public void run() {
		Round round = ModelFactory.getInstance().createRound(category.getRounds().size()+1);
		category.addRound(round);
		categoryMasterDetail.refresh();
		categoryMasterDetail.setEditorDirty();
	}
}
