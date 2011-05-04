package ch.jester.ui.handlers;

import java.util.Iterator;
import java.util.List;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;


import ch.jester.common.ui.handlers.AbstractCommandHandler;
import ch.jester.common.ui.services.IEditorService;
import ch.jester.commonservices.api.logging.ILogger;
import ch.jester.model.Player;
import ch.jester.ui.Activator;
import ch.jester.ui.player.editor.ctrl.PlayerListController;

public class DeletePlayerHandler extends AbstractCommandHandler {
	ILogger logger;
	public DeletePlayerHandler(){
	 	logger = Activator.getDefault().getActivationContext().getLogger();
	}
	@Override
	public Object executeInternal(ExecutionEvent event) {
		Job job = new Job("Deleting"){

			@Override
			public IStatus run(IProgressMonitor monitor) {
				delete(monitor);
				return Status.OK_STATUS;
			}
			
			
		};
		//job.setUser(true);
		job.schedule();
	
		return null;
	}
	
	private Object delete(IProgressMonitor monitor){
		ISelection selection = getSelection();
		monitor.setTaskName("Deleting Players");
		monitor.beginTask("deleting", getSelectionCount()*2);
		PlayerListController model = getServiceUtil().getService(PlayerListController.class);
		IEditorService editors = getServiceUtil().getService(IEditorService.class);
		if(isIStructuredSelection()){
			IStructuredSelection structSel = (IStructuredSelection) selection;
			List<Player> list = structSel.toList();
			model.removePlayer(list);
			monitor.worked(getSelectionCount());
			monitor.done();
			Iterator<Object> selectionIterator = structSel.iterator();
			while(selectionIterator.hasNext()){
				Object select = selectionIterator.next();
				editors.closeEditor(select);
			}
		}
		return null;
	}
}
