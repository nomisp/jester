package ch.jester.common.ui.handlers;

import java.util.Iterator;
import java.util.List;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;


import ch.jester.common.ui.databinding.DaoController;
import ch.jester.common.ui.internal.Activator;
import ch.jester.common.ui.services.IEditorService;
import ch.jester.common.utility.AdapterUtility;
import ch.jester.commonservices.api.logging.ILogger;
import ch.jester.commonservices.api.persistency.IEntityObject;


public class DaoDeleteHandler extends AbstractCommandHandler {
	ILogger logger;
	public DaoDeleteHandler(){
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
	
	@SuppressWarnings("unchecked")
	private Object delete(IProgressMonitor monitor){
		ISelection selection = getSelection();
		monitor.setTaskName("Deleting...");
		monitor.beginTask("deleting", getSelectionCount()*2);
		DaoController<IEntityObject> ctrl = AdapterUtility.getAdaptedObject(getActivePartFromEvent(), DaoController.class);
		IEditorService editors = getServiceUtil().getService(IEditorService.class);
		if(isIStructuredSelection()){
			IStructuredSelection structSel = (IStructuredSelection) selection;
			List<IEntityObject> list = structSel.toList();
			ctrl.removeDaoObject(list);
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
