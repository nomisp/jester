package ch.jester.ui.handlers;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.jface.viewers.StructuredViewer;

import ch.jester.common.ui.utility.AbstractEventHandler;
import ch.jester.common.utility.AdapterUtility;
import ch.jester.dao.IPlayerPersister;
import ch.jester.ui.player.PlayerListModel;

public class LoadPlayers extends AbstractEventHandler {
	@Override
	public Object executeInternal(ExecutionEvent event) {
		StructuredViewer viewer = AdapterUtility.getAdaptedObject(getActivePartFromEvent(), StructuredViewer.class);
		if(viewer!=null){
			IPlayerPersister persister = getServiceUtil().getExclusiveService(IPlayerPersister.class);
			PlayerListModel model = getServiceUtil().getService(PlayerListModel.class);
			model.clear();
			model.addPlayer(persister.getAll());
		
		}
		
		return null;
	}

}
