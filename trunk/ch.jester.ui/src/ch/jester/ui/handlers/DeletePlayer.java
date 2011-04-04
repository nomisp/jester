package ch.jester.ui.handlers;

import java.util.Iterator;
import java.util.List;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;

import ch.jester.common.ui.services.IEditorService;
import ch.jester.common.ui.utility.AbstractEventHandler;
import ch.jester.commonservices.api.logging.ILogger;
import ch.jester.dao.IPlayerPersister;
import ch.jester.model.Player;
import ch.jester.ui.Activator;
import ch.jester.ui.player.PlayerListModel;

public class DeletePlayer extends AbstractEventHandler {
	ILogger logger;
	public DeletePlayer(){
	 	logger = Activator.getDefault().getActivationContext().getLogger();
	}
	@Override
	public Object executeInternal(ExecutionEvent event) {
		ISelection selection = getSelection();
		IPlayerPersister persister = getServiceUtil().getExclusiveService(IPlayerPersister.class);
		PlayerListModel model = getServiceUtil().getService(PlayerListModel.class);
		IEditorService editors = getServiceUtil().getService(IEditorService.class);
		if(isIStructuredSelection()){
			IStructuredSelection structSel = (IStructuredSelection) selection;
			List<Player> list = structSel.toList();
			model.removePlayer(list);
			persister.delete(list);
			
			Iterator<Object> selectionIterator = structSel.iterator();
			while(selectionIterator.hasNext()){
				Object select = selectionIterator.next();

				editors.closeEditor(select);
				logger.debug("Player "+select+" removed from DB");
			}
		}else{
			
			
		}
		return null;
	}
}
