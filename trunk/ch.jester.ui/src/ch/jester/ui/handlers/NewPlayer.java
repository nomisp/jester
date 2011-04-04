package ch.jester.ui.handlers;

import org.eclipse.core.commands.ExecutionEvent;

import ch.jester.common.ui.services.IEditorService;
import ch.jester.common.ui.utility.AbstractEventHandler;
import ch.jester.commonservices.api.logging.ILogger;
import ch.jester.dao.IPlayerPersister;
import ch.jester.model.Player;
import ch.jester.ui.Activator;
import ch.jester.ui.player.PlayerListModel;

public class NewPlayer extends AbstractEventHandler {
	ILogger logger;
	public NewPlayer(){
		logger = Activator.getDefault().getActivationContext().getLogger();
	}
	
	@Override
	public Object executeInternal(ExecutionEvent event) {
		IPlayerPersister persister = getServiceUtil().getExclusiveService(IPlayerPersister.class);
		Player player = new Player();
		player.setCity(" ");
		player.setNation(" ");
		player.setFirstName(" ");
		player.setLastName("NewPlayer");
		getServiceUtil().getService(PlayerListModel.class).addPlayer(player);
		getServiceUtil().getService(IEditorService.class).openEditor(player);
		persister.save(player);
		logger.debug("Player "+player+ " saved." );
		
		return null;
	}

}
