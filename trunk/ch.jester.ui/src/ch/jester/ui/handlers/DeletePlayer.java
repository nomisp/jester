package ch.jester.ui.handlers;

import java.util.Iterator;

import org.eclipse.core.commands.ExecutionEvent;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;

import ch.jester.common.ui.utility.AbstractEventHandler;
import ch.jester.common.utility.ServiceUtility;
import ch.jester.commonservices.api.logging.ILogger;
import ch.jester.dao.IPlayerPersister;
import ch.jester.model.Player;
import ch.jester.ui.Activator;

public class DeletePlayer extends AbstractEventHandler {
	ILogger logger;
	public DeletePlayer(){
	 	logger = Activator.getDefault().getActivationContext().getLogger();
	}
	@Override
	public Object executeInternal(ExecutionEvent event) {
		ISelection selection = getSelection();
		ServiceUtility su = Activator.getDefault().getActivationContext().getServiceUtil();
		IPlayerPersister persister = su.getExclusiveService(IPlayerPersister.class);
		if(isIStructuredSelection()){
			IStructuredSelection structSel = (IStructuredSelection) selection;
			Iterator<Object> selectionIterator = structSel.iterator();
			while(selectionIterator.hasNext()){
				Object select = selectionIterator.next();
				persister.delete((Player)select);
				logger.debug("Player "+select+" removed from DB");
			}
		}else{
			
			
		}
		return null;
	}
}
