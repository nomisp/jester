package ch.jester.ui.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;

import ch.jester.common.utility.ServiceUtility;
import ch.jester.dao.IPlayerPersister;

public class LoadPlayers extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
	
		IWorkbenchPart view = getActiveView();
		StructuredViewer viewer = (StructuredViewer)view.getAdapter(StructuredViewer.class);
		if(viewer!=null){
			ServiceUtility su = new ServiceUtility();
			IPlayerPersister persister = su.getExclusiveService(IPlayerPersister.class);
			viewer.setInput(persister.getAll());
		
		}
		
		return null;
	}
	
	private IWorkbenchPart getActiveView(){
		return PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActivePart();
	}

}
