package ch.jester.common.ui.utility;

import org.eclipse.core.runtime.ISafeRunnable;
import org.eclipse.jface.dialogs.MessageDialog;

import ch.jester.common.ui.internal.CommonUIActivator;
import ch.jester.common.utility.ExceptionUtility;
import ch.jester.common.utility.ExceptionWrapper;
import ch.jester.commonservices.api.logging.ILogger;
import ch.jester.commonservices.exceptions.ProcessingException;

/**
 * Sicherer Runner, welcher eine DialogBox zeigt, sollte ein Fehler auftreten.
 * Ist der Fehler eine ProcessingExcpetion, wird diese nach der wirklichken Ursache durchsucht. 
 *
 */
public abstract class SafeMessageBoxRunner implements ISafeRunnable {
	ILogger mLogger = CommonUIActivator.getDefault().getActivationContext().getLogger();
	@Override
	public void handleException(Throwable exception) {
		final ExceptionWrapper ew = ExceptionUtility.wrap(exception, ProcessingException.class);
		UIUtility.syncExecInUIThread(new Runnable() {
			@Override
			public void run() {
				MessageDialog.openError(UIUtility.getActiveWorkbenchWindow().getShell(), "Error", ew.getThrowableMessage());
				mLogger.error(ew.getThrowable());
			}
		});
		
		
	
		
	}


}
