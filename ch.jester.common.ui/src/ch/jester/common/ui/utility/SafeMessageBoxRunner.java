package ch.jester.common.ui.utility;

import org.eclipse.core.runtime.ISafeRunnable;
import org.eclipse.jface.dialogs.MessageDialog;

import ch.jester.common.utility.ExceptionUtility;
import ch.jester.common.utility.ExceptionWrapper;
import ch.jester.commonservices.exceptions.ProcessingException;

public abstract class SafeMessageBoxRunner implements ISafeRunnable {

	@Override
	public void handleException(Throwable exception) {
		final ExceptionWrapper ew = ExceptionUtility.wrap(exception, ProcessingException.class);
		UIUtility.syncExecInUIThread(new Runnable() {
			@Override
			public void run() {
				MessageDialog.openError(UIUtility.getActiveWorkbenchWindow().getShell(), "Error", ew.getThrowableMessage());
				
			}
		});
		
		
	
		
	}


}
