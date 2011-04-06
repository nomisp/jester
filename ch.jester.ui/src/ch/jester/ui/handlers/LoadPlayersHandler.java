package ch.jester.ui.handlers;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.jface.viewers.StructuredViewer;

import ch.jester.common.ui.utility.AbstractEventHandler;
import ch.jester.common.utility.AdapterUtility;
import ch.jester.dao.IPlayerPersister;
import ch.jester.ui.player.editor.PlayerListController;

public class LoadPlayersHandler extends AbstractEventHandler {
	@Override
	public Object executeInternal(ExecutionEvent event) {
		StructuredViewer viewer = AdapterUtility.getAdaptedObject(getActivePartFromEvent(), StructuredViewer.class);
		if(viewer!=null){
			IPlayerPersister persister = getServiceUtil().getExclusiveService(IPlayerPersister.class);
			PlayerListController model = getServiceUtil().getService(PlayerListController.class);
			model.clear();
			model.addPlayer(persister.getAll());
		
		}
		
		return null;
	}

}
