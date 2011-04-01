package ch.jester.common.ui.utility;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;

public abstract class AbstractEventHandler extends AbstractHandler {
	ExecutionEvent mEvent;
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		mEvent = event;
		return executeInternal(event);
	}
	public abstract Object executeInternal(ExecutionEvent event);
	public ISelection getSelection(){
		return  HandlerUtil.getActiveWorkbenchWindow(mEvent).getActivePage().getSelection();
	}
	
	public Shell getShell(){
		return HandlerUtil.getActiveWorkbenchWindow(mEvent).getShell();
	}
	
	public boolean isIStructuredSelection(){
		return (getSelection() instanceof IStructuredSelection);
	}
	public Object getFirstSelected(){
		if(isIStructuredSelection()){
			IStructuredSelection selection = (IStructuredSelection) getSelection();
			return selection.getFirstElement();
		}
		return null;
	}
	public IWorkbenchPart getActivePart(){
		return PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActivePart();
	}
	
	public IWorkbenchPart getActivePartFromEvent(){
		return HandlerUtil.getActivePart(mEvent);
	}
	
	@SuppressWarnings("unchecked")
	public <T> T getFirstSelectedAs(Class<T> clz){
		if(getFirstSelected()!=null){
			if(getFirstSelected().getClass().isAssignableFrom(clz)){
				return (T)getFirstSelected();
			}
		}
		return null;
	}
}
