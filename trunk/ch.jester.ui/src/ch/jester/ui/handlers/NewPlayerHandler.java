package ch.jester.ui.handlers;

import org.eclipse.core.commands.ExecutionEvent;

import ch.jester.common.ui.services.IEditorService;
import ch.jester.common.ui.utility.AbstractEventHandler;
import ch.jester.commonservices.api.logging.ILogger;
import ch.jester.dao.IPlayerPersister;
import ch.jester.model.Player;
import ch.jester.ui.Activator;
import ch.jester.ui.player.editor.PlayerListController;

public class NewPlayerHandler extends AbstractEventHandler {
	ILogger logger;
	public NewPlayerHandler(){
		logger = Activator.getDefault().getActivationContext().getLogger();
	}
	
	@Override
	public Object executeInternal(ExecutionEvent event) {
		IPlayerPersister persister = getServiceUtil().getExclusiveService(IPlayerPersister.class);
		Player player = new Player();
		player.setCity("City");
		player.setNation("Nation");
		player.setFirstName("FirstName");
		player.setLastName("LastName");
		//hinzufügen vom Player
		getServiceUtil().getService(PlayerListController.class).addPlayer(player);
		//selektiert im UI
		setSelection(player);
		//öffne im Editor
		getServiceUtil().getService(IEditorService.class).openEditor(player);
		persister.save(player);
		logger.debug("Player "+player+ " saved." );
		
		return null;
	}

}
