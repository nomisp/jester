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
public class AddCategoryAction extends Action {
	private Tournament tournament;
	private CategoryMasterDetail categoryMasterDetail;
	
	public AddCategoryAction(Tournament tournament, CategoryMasterDetail categoryMDBlock) {
		this.tournament = tournament;
		this.categoryMasterDetail = categoryMDBlock;
	}
	
	@Override
	public String getText() {
		return Messages.Action_AddCategory_text;
	}

	@Override
	public void run() {
		Category cat = ModelFactory.getInstance().createCategory(Messages.Action_entityCategory_name + (tournament.getCategories().size()+1));
		tournament.addCategory(cat);
		categoryMasterDetail.refresh();
		categoryMasterDetail.setEditorDirty();
	}
}
