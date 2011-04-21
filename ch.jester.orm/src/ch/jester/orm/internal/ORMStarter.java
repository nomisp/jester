package ch.jester.orm.internal;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IStartup;

import ch.jester.common.ui.utility.UIUtility;
import ch.jester.orm.ORMPlugin;

/**
 * Implementiert IStartup.
 * Dadurch wird die EntityManagerFactory beim Starten generiert
 *
 */
public class ORMStarter implements IStartup{

	@Override
	public void earlyStartup() {
		//initialisierung
		UIUtility.syncExecInUIThread(new Runnable(){

			@Override
			public void run() {
				Shell shell = Display.getDefault().getActiveShell();
				ProgressMonitorDialog dialog = new ProgressMonitorDialog(shell); 
				try {
					dialog.run(true, false, new IRunnableWithProgress(){ 
					    public void run(IProgressMonitor monitor) { 
					    	monitor.beginTask("Initialize Database... please be patient", IProgressMonitor.UNKNOWN);
							ORMPlugin.getJPAEntitManagerFactor();
							monitor.done();
					    } 
					});
				} catch (InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			
		});
	}
}
