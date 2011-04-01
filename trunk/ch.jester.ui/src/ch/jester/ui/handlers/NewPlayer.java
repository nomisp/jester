package ch.jester.ui.handlers;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.handlers.HandlerUtil;

import ch.jester.common.ui.utility.AbstractEventHandler;
import ch.jester.common.utility.ServiceUtility;
import ch.jester.commonservices.api.logging.ILogger;
import ch.jester.dao.IPlayerPersister;
import ch.jester.model.Player;
import ch.jester.ui.Activator;
import ch.jester.ui.player.PlayerEditor;

public class NewPlayer extends AbstractEventHandler {
	ILogger logger;
	public NewPlayer(){
		logger = Activator.getDefault().getActivationContext().getLogger();
	}
	
	@Override
	public Object executeInternal(ExecutionEvent event) {
		ServiceUtility su = Activator.getDefault().getActivationContext().getServiceUtil();
		IPlayerPersister persister = su.getExclusiveService(IPlayerPersister.class);
		Player player = new Player();
		player.setCity("TestCity");
		player.setNation("TestNation");
		player.setFirstName("TestFirst");
		player.setLastName("TestLast");
		IWorkbenchPage page = HandlerUtil.getActiveWorkbenchWindow(event).getActivePage();
		try {
			page.openEditor(new PlayerInput(player), PlayerEditor.ID);
		} catch (PartInitException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		persister.save(player);
		logger.debug("Player "+player+ " saved." );
		
		return null;
	}

}
