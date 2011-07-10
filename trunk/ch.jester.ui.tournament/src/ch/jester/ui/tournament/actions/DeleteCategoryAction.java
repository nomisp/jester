package ch.jester.ui.tournament.actions;

import org.eclipse.jface.action.Action;

import ch.jester.model.Category;
import ch.jester.model.Round;
import ch.jester.model.Tournament;
import ch.jester.model.factories.ModelFactory;
import ch.jester.ui.tournament.forms.CategoryMasterDetail;
import ch.jester.ui.tournament.nl1.Messages;

/**
 * Action um Runden zu einem Turnier hinzuzufügen
 * @author Peter
 *
 */
public class DeleteCategoryAction extends Action {
	private Category cat;
	private CategoryMasterDetail categoryMasterDetail;
	
	public DeleteCategoryAction(Category cat, CategoryMasterDetail categoryMDBlock) {
		this.cat = cat;
		this.categoryMasterDetail = categoryMDBlock;
	}
	
	@Override
	public String getText() {
		return Messages.Action_DeleteCategory_text;
	}

	@Override
	public void run() {
		cat.getTournament().removeCategory(cat);
		categoryMasterDetail.refresh();
		categoryMasterDetail.setEditorDirty();
	}
}
