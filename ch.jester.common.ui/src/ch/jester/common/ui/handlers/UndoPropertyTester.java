package ch.jester.common.ui.handlers;

import org.eclipse.core.commands.operations.IUndoContext;
import org.eclipse.core.expressions.PropertyTester;
import org.eclipse.ui.PlatformUI;

import ch.jester.common.ui.internal.Activator;
import ch.jester.commonservices.api.logging.ILogger;

public class UndoPropertyTester extends PropertyTester {
	private ILogger mLogger;
	public UndoPropertyTester() {
		mLogger = Activator.getDefault().getActivationContext().getLogger();
	}


	
	public boolean test(Object receiver, String property, Object[] args,
			Object expectedValue) {
		IUndoContext mUndoContext = PlatformUI.getWorkbench().getOperationSupport().getUndoContext();
		boolean b =  PlatformUI.getWorkbench().getOperationSupport().getOperationHistory().canUndo(mUndoContext);
		//System.out.println(b);
		return b;
	}
	

}
