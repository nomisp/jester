package ch.jester.ui.handlers;

import org.eclipse.core.commands.ExecutionEvent;

import ch.jester.common.ui.handlers.AbstractCommandHandler;
import ch.jester.commonservices.api.logging.ILogger;
import ch.jester.model.Player;
import ch.jester.model.factories.PlayerFactory;
import ch.jester.ui.Activator;
import ch.jester.ui.player.editor.PlayerListController;
import ch.jester.ui.player.editor.PlayersView;

public class NewPlayerHandler extends AbstractCommandHandler {
	ILogger logger;
	public NewPlayerHandler(){
		logger = Activator.getDefault().getActivationContext().getLogger();
	}
	
	@Override
	public Object executeInternal(ExecutionEvent event) {
		//Defaultplayer
		Player player = PlayerFactory.createPlayer();
		
		//hinzufügen vom Player
		getServiceUtil().getService(PlayerListController.class).addPlayer(player);
	
		//selektiert im UI
		setSelection(PlayersView.ID, player);

		//öffne Editor
		openEditor(player);

		
		return null;
	}

}
