package ch.jester.ui.handlers;

import ch.jester.common.ui.handlers.DaoAddHandler;
import ch.jester.commonservices.api.persistency.IDaoObject;
import ch.jester.model.factories.ModelFactory;
import ch.jester.ui.player.editor.view.PlayersView;

public class AddPlayerHandler extends DaoAddHandler{

	@Override
	public String getTargetViewId() {
		return PlayersView.ID;
	}

	@Override
	public IDaoObject getNewDaoObject() {
		return ModelFactory.getInstance().createPlayer();
	};
			

}
