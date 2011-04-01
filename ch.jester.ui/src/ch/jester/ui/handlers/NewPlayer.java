package ch.jester.ui.handlers;

import org.eclipse.core.commands.ExecutionEvent;

import ch.jester.common.ui.utility.AbstractEventHandler;
import ch.jester.common.utility.ServiceUtility;
import ch.jester.commonservices.api.logging.ILogger;
import ch.jester.dao.IPlayerPersister;
import ch.jester.model.Player;
import ch.jester.ui.Activator;

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
		persister.save(player);
		logger.debug("Player "+player+ " saved." );
		
		return null;
	}

}
