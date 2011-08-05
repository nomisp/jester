package ch.jester.common.ui.handlers;

import org.eclipse.core.commands.operations.IUndoContext;
import org.eclipse.core.expressions.PropertyTester;
import org.eclipse.ui.PlatformUI;

public class UndoPropertyTester extends PropertyTester {
	public boolean test(Object receiver, String property, Object[] args,
			Object expectedValue) {
		IUndoContext mUndoContext = PlatformUI.getWorkbench().getOperationSupport().getUndoContext();
		boolean b =  PlatformUI.getWorkbench().getOperationSupport().getOperationHistory().canUndo(mUndoContext);
		return b;
	}
	

}
