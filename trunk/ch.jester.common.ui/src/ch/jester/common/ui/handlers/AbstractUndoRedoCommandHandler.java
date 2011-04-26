package ch.jester.common.ui.handlers;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.operations.IOperationHistory;
import org.eclipse.core.commands.operations.IUndoContext;
import org.eclipse.core.commands.operations.IUndoableOperation;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.operations.IWorkbenchOperationSupport;


public abstract class AbstractUndoRedoCommandHandler extends AbstractCommandHandler {
	private IWorkbenchOperationSupport mOperationSupport = PlatformUI.getWorkbench().getOperationSupport();
	private IOperationHistory mOperationHistory = mOperationSupport.getOperationHistory();
	private IUndoContext mUndoContext = mOperationSupport.getUndoContext();
	private IUndoableOperation mCurrentOperation;
	@Override
	public Object executeInternal(ExecutionEvent event) {
		mCurrentOperation = null;
		mCurrentOperation = getOperation();
		mCurrentOperation.addContext(mUndoContext);
		try {
			mOperationHistory.execute(mCurrentOperation, new NullProgressMonitor(), null);
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	protected abstract IUndoableOperation getOperation();
	
/*	protected AbstractOperation getOperation(){
				Object trigger = super.mEvent.getTrigger();
				try {
					IParameter params = super.mEvent.getCommand().getParameter("ch.jester.ui.operation");
					IParameterValues values = params.getValues();
					Object val = values.getParameterValues().get("ch.jester.undoablecommandclass");
					System.out.println(params);
				} catch (NotDefinedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (ParameterValuesException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				String operation = super.mEvent.getParameter("ch.jester.ui.operations");
				Map map =super.mEvent.getParameters();
				Object op=null;
				try {
					op = super.mEvent.getObjectParameterForExecution("ch.jester.ui.operations");
				} catch (ExecutionException e) {
					// TODO Auto-generated catch block
					
					int k = 0;
					k++;
					k++;
					e.printStackTrace();
				}
				System.out.println(op);
		
			return null;
	}*/
	
	
}
