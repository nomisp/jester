package ch.jester.ui.handlers;

import org.eclipse.core.commands.ExecutionEvent;

import ch.jester.common.ui.handlers.AbstractCommandHandler;
import ch.jester.commonservices.util.ServiceUtility;
import ch.jester.ui.contentprovider.PageController;
import ch.jester.ui.player.editor.ctrl.PlayerListController;

public class PageHandler extends AbstractCommandHandler {
	ServiceUtility su = new ServiceUtility();
	@Override
	public Object executeInternal(ExecutionEvent event) {
		PageController<?> controller = su.getService(PlayerListController.class).getPageController();
		
		
		if(event.getCommand().getId().contains("back")){
			controller.prevPage();
		}else{
			controller.nextPage();
		}
		return null;
	}

}
