package ch.jester.common.ui.handlers;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.operations.IOperationHistory;
import org.eclipse.core.commands.operations.IUndoContext;
import org.eclipse.core.commands.operations.IUndoableOperation;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.operations.IWorkbenchOperationSupport;


/**
 * Abstrakter UndoRedo Handler.
 * Noch nirgends verwendet.
 *
 */
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
			e.printStackTrace();
		}
		return null;
	}
	
	protected abstract IUndoableOperation getOperation();
	
	
}
