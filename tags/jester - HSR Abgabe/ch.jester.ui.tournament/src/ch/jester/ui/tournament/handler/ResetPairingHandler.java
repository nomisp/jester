package ch.jester.ui.tournament.handler;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.commands.ExecutionEvent;

import ch.jester.common.ui.handlers.AbstractCommandHandler;
import ch.jester.model.Category;
import ch.jester.model.Round;
import ch.jester.model.Tournament;

/**
 * Handler um Paarungen zu löschen
 *
 */
public class ResetPairingHandler extends AbstractCommandHandler{

	@Override
	public Object executeInternal(ExecutionEvent event) {
		final List<Category> categoriesToReset = new ArrayList<Category>();
		final Category cat = getFirstSelectedAs(Category.class);
		final Tournament tournament;
		if (cat == null) {
			tournament = getFirstSelectedAs(Tournament.class);
			categoriesToReset.addAll(tournament.getCategories());
		} else {
			tournament = cat.getTournament();
			categoriesToReset.add(cat);

		}
		resetPairingsAndRounds(categoriesToReset);
		if(tournament.getPairedCategories().size()==0){
			tournament.setStarted(false);
		}
		getServiceUtil().getDaoServiceByEntity(Tournament.class).save(tournament);
		return null;
	}

	/**
	 * @param cats
	 * @return Anzahl resetted Categories, und zwar in Bezug auf eine List 
	 */
	private int resetPairingsAndRounds(List<Category> cats) {
		int resetted = -1;
		for(Category cat:cats){
			resetted++;
			for(Round r:cat.getRounds()){
				r.removeAllPairings(r.getPairings());
			}
			cat.removeRounds();
		}
		return resetted;
	}
}
