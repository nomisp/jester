package ch.jester.common.ui.handlers;

import java.sql.BatchUpdateException;
import java.util.Iterator;
import java.util.List;


import messages.Messages;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;

import ch.jester.common.ui.handlers.api.IHandlerDelete;
import ch.jester.common.ui.internal.CommonUIActivator;
import ch.jester.common.ui.services.IEditorService;
import ch.jester.common.utility.AdapterUtility;
import ch.jester.common.utility.ExceptionUtility;
import ch.jester.common.utility.ExceptionWrapper;
import ch.jester.commonservices.api.persistency.IEntityObject;
import ch.jester.commonservices.exceptions.ProcessingException;


public class DaoDeleteHandler extends AbstractCommandHandler {
	private static final String LBL_DELETING = Messages.DaoDeleteHandler_deleting;

	@Override
	public Object executeInternal(ExecutionEvent event) {
		Job job = new Job(LBL_DELETING){

			@Override
			public IStatus run(IProgressMonitor monitor) {
				try{
					delete(monitor);
				}catch(ProcessingException e){
					ExceptionWrapper wrapper = ExceptionUtility.wrap(e);
					Throwable tt = wrapper.getRootThrowable();
					String msg = wrapper.getThrowableMessage();
					if(tt instanceof BatchUpdateException){
						msg = Messages.DaoDeleteHandler_still_referenced;
					}
					return new Status(IStatus.ERROR, CommonUIActivator.getDefault().getActivationContext().getPluginId(),msg , e);
				}
				return Status.OK_STATUS;
			}
			
			
		};
		//job.setUser(true);
		job.schedule();
		
	
		return null;
	}
	
	@SuppressWarnings("unchecked")
	private Object delete(IProgressMonitor monitor) throws ProcessingException{
		ISelection selection = getSelection();
		monitor.setTaskName(LBL_DELETING);
		monitor.beginTask(LBL_DELETING, getSelectionCount()*2);
		IHandlerDelete<IEntityObject> ctrl = AdapterUtility.getAdaptedObject(getActivePartFromEvent(), IHandlerDelete.class);
		if(ctrl==null){
			throw new RuntimeException("No IHandlerDelete found for: "+getActivePartFromEvent()); //$NON-NLS-1$
		}
		IEditorService editors = getServiceUtil().getService(IEditorService.class);
		if(isIStructuredSelection()){
			IStructuredSelection structSel = (IStructuredSelection) selection;
			List<IEntityObject> list = structSel.toList();
			ctrl.handleDelete(list);
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
