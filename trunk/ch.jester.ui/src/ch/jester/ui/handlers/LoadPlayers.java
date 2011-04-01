package ch.jester.ui.handlers;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.jface.viewers.StructuredViewer;

import ch.jester.common.ui.utility.AbstractEventHandler;
import ch.jester.common.utility.AdapterUtility;
import ch.jester.common.utility.ServiceUtility;
import ch.jester.dao.IPlayerPersister;
import ch.jester.ui.Activator;

public class LoadPlayers extends AbstractEventHandler {
	@Override
	public Object executeInternal(ExecutionEvent event) {
		StructuredViewer viewer = AdapterUtility.getAdaptedObject(getActivePartFromEvent(), StructuredViewer.class);
		if(viewer!=null){
			ServiceUtility su = Activator.getDefault().getActivationContext().getServiceUtil();
			IPlayerPersister persister = su.getExclusiveService(IPlayerPersister.class);
			viewer.setInput(persister.getAll());
		
		}
		
		return null;
	}

}
