package ch.jester.ui.tournament.actions;

import messages.Messages;

import org.eclipse.jface.action.Action;

import ch.jester.model.Category;
import ch.jester.ui.tournament.forms.CategoryMasterDetail;

/**
 * Action um Runden zu einem Turnier hinzuzufügen
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
