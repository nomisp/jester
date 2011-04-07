package ch.jester.ui.handlers;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.ui.IViewPart;

import ch.jester.common.ui.handlers.AbstractCommandHandler;
import ch.jester.common.ui.services.IEditorService;
import ch.jester.common.ui.utility.UIUtility;
import ch.jester.commonservices.api.logging.ILogger;
import ch.jester.dao.IPlayerPersister;
import ch.jester.model.Player;
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
		IPlayerPersister persister = getServiceUtil().getExclusiveService(IPlayerPersister.class);
		Player player = new Player();
		player.setCity("City");
		player.setNation("Nation");
		player.setFirstName("FirstName");
		player.setLastName("LastName");
		
		//hinzufügen vom Player
		getServiceUtil().getService(PlayerListController.class).addPlayer(player);
	
		//selektiert im UI
		PlayersView viewpart =(PlayersView) UIUtility.getActiveWorkbenchWindow().getActivePage().findView(PlayersView.ID);
		setSelection(viewpart, player);
		

		//öffne Editor
		getServiceUtil().getService(IEditorService.class).openEditor(player);
		persister.save(player);
		
		logger.debug("Player "+player+ " saved." );
		
		return null;
	}

}
