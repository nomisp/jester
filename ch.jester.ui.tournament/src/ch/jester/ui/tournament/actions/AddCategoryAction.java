package ch.jester.ui.tournament.actions;

import messages.Messages;

import org.eclipse.jface.action.Action;

import ch.jester.model.Category;
import ch.jester.model.Tournament;
import ch.jester.model.factories.ModelFactory;
import ch.jester.ui.tournament.forms.CategoryMasterDetail;

/**
 * Action um Runden zu einem Turnier hinzuzufügen
 *
 */
public class AddCategoryAction extends Action {
	private Tournament tournament;
	private CategoryMasterDetail categoryMasterDetail;
	private static int creationCounter=-1;
	public AddCategoryAction(Tournament tournament, CategoryMasterDetail categoryMDBlock) {
		this.tournament = tournament;
		this.categoryMasterDetail = categoryMDBlock;
		if(creationCounter==-1){
			creationCounter = tournament.getCategories().size();
		}
	}
	
	@Override
	public String getText() {
		return Messages.Action_AddCategory_text;
	}

	@Override
	public void run() {
		creationCounter++;
		Category cat = ModelFactory.getInstance().createCategory(Messages.Action_entityCategory_name + creationCounter);
		tournament.addCategory(cat);
		categoryMasterDetail.refresh();
		categoryMasterDetail.setEditorDirty();
	}
}
