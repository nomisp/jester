package ch.jester.common.ui.handlers;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.operations.AbstractOperation;
import org.eclipse.core.commands.operations.IUndoContext;
import org.eclipse.core.commands.operations.IUndoableOperation;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.ui.PlatformUI;


public abstract class AbstractUndoRedoCommandHandler extends AbstractCommandHandler implements IUndoableOperation{
	AbstractOperation mDefaultOperation = new AbstractOperation(getLabel()){
		@Override
		public IStatus execute(IProgressMonitor monitor, IAdaptable info)
				throws ExecutionException {
			return AbstractUndoRedoCommandHandler.this.execute(monitor, info);
		}

		@Override
		public IStatus redo(IProgressMonitor monitor, IAdaptable info)
				throws ExecutionException {
			return AbstractUndoRedoCommandHandler.this.redo(monitor, info);
		}

		@Override
		public IStatus undo(IProgressMonitor monitor, IAdaptable info)
				throws ExecutionException {
			return AbstractUndoRedoCommandHandler.this.undo(monitor, info);
		}
		
	};
	@Override
	public Object executeInternal(ExecutionEvent event) {
		IUndoContext context = PlatformUI.getWorkbench().getOperationSupport().getUndoContext();
		IUndoableOperation op;
		op = mDefaultOperation;
		op.addContext(context);
		try {
			PlatformUI.getWorkbench().getOperationSupport().getOperationHistory().execute(this, new NullProgressMonitor(), null);
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void addContext(IUndoContext context) {
		mDefaultOperation.addContext(context);
	}

	@Override
	public boolean canExecute() {
		return mDefaultOperation.canExecute();
	}

	@Override
	public boolean canRedo() {
		return mDefaultOperation.canRedo();
	}

	@Override
	public boolean canUndo() {
		return mDefaultOperation.canUndo();
	}

	@Override
	public IUndoContext[] getContexts() {
		return mDefaultOperation.getContexts();
	}

	@Override
	public String getLabel() {
		return "";
	}

	@Override
	public boolean hasContext(IUndoContext context) {
		return mDefaultOperation.hasContext(context);
	}


	@Override
	public void removeContext(IUndoContext context) {
		mDefaultOperation.removeContext(context);
	}



}
