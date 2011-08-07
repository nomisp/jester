package ch.jester.rcpapplication;

import java.io.IOException;

import messages.Messages;

import org.eclipse.core.runtime.Platform;
import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;
import org.eclipselabs.p2.rcpupdate.utils.P2Util;

/**
 * Jester - Standard Eclipse Application Implementation.
 * Start Dispatching UI Events etc.
 *
 */
public class Application implements IApplication {
	public Object start(IApplicationContext context) {
		boolean locked = false;
		try {
			locked = Platform.getInstanceLocation().lock();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Display display = PlatformUI.createDisplay();
		if(!locked){
			MessageDialog.openError(display.getActiveShell(), Messages.Application_running_title, Messages.Application_running_text);
			return IApplication.EXIT_OK;
		}
		try {
			setupJester();
			int returnCode = PlatformUI.createAndRunWorkbench(display, new ApplicationWorkbenchAdvisor());
			Platform.getInstanceLocation().release();
			if (returnCode == PlatformUI.RETURN_RESTART) {
				return IApplication.EXIT_RESTART;
			}
			return IApplication.EXIT_OK;
		} finally {
			display.dispose();
		}
	}

	public void stop() {
		if (!PlatformUI.isWorkbenchRunning())
			return;
		final IWorkbench workbench = PlatformUI.getWorkbench();
		final Display display = workbench.getDisplay();
		display.syncExec(new Runnable() {
			public void run() {
				if (!display.isDisposed())
					workbench.close();
			}
		});
	}

	/**
	 * Repo URL hinzufügen.
	 */
	private void setupJester() {
		//adding repos
		P2Util.addRepository("http://jester.googlecode.com/svn/jesterrepo"); //$NON-NLS-1$
		
	}

}
