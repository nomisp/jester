package ch.jester.ui.handlers;

import org.eclipse.ui.IViewPart;

import ch.jester.common.ui.databinding.DaoController;
import ch.jester.common.ui.handlers.AbstractCommandHandler;
import ch.jester.common.utility.AdapterUtility;
import ch.jester.commonservices.api.persistency.IDaoObject;
import ch.jester.model.Player;
import ch.jester.model.factories.ModelFactory;
import ch.jester.ui.player.editor.view.PlayersView;

public class AddPlayerHandler extends AbstractCommandHandler{
	public Object executeInternal(org.eclipse.core.commands.ExecutionEvent event) {
		Player player = ModelFactory.getInstance().createPlayer();
		
		//holen der Target View
		IViewPart view = getView(PlayersView.ID);
		
		//Wir gehen davon aus, dass sich die View an der Platform mit einem Controller angemeldet hat,
		//und holen diesen
		DaoController<IDaoObject> ctrl =  AdapterUtility.getAdaptedObject(view, DaoController.class);
		
		//hinzufügen vom Player
		ctrl.addDaoObject(player);
	
		//selektiert im UI
		setSelection(view, player);

		//öffne Editor
		ctrl.openEditor(player);

		
		return null;
		
		
	};
			

}
