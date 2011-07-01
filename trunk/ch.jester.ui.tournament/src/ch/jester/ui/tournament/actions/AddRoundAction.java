package ch.jester.ui.tournament.actions;

import org.eclipse.jface.action.Action;

import ch.jester.model.Category;
import ch.jester.model.Round;
import ch.jester.model.factories.ModelFactory;

/**
 * Action um Runden zu einem Turnier hinzuzufügen
 * @author Peter
 *
 */
public class AddRoundAction extends Action {
	private Category category;
	
	public AddRoundAction(Category cat) {
		this.category = cat;
	}
	
	@Override
	public String getText() {
		return "AddRound";
	}

	@Override
	public void run() {
		Round round = ModelFactory.getInstance().createRound(category.getRounds().size()+1);
		category.addRound(round);
	}
}
