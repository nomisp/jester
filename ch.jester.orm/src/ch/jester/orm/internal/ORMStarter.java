package ch.jester.orm.internal;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.action.StatusLineContributionItem;
import org.eclipse.jface.action.StatusLineManager;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IStartup;
import org.eclipse.ui.IWorkbenchActionConstants;

import ch.jester.common.ui.databinding.DaoController;
import ch.jester.common.ui.services.IExtendedStatusLineManager;
import ch.jester.common.ui.utility.UIUtility;
import ch.jester.common.utility.AdapterUtility;
import ch.jester.commonservices.api.persistency.IDBStartupListener;
import ch.jester.commonservices.util.ServiceUtility;
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
							final StatusLineContributionItem ss =new StatusLineContributionItem("LoggedInStatus");
							ORMPlugin.getDefault().getActivationContext().getService(IExtendedStatusLineManager.class).appendToGroup(StatusLineManager.END_GROUP, ss);
							String[] str = Platform.getAdapterManager().computeAdapterTypes(IDBStartupListener.class);
							ServiceUtility su = new ServiceUtility();
							
							List<IDBStartupListener> listeners = su.getServices(IDBStartupListener.class);
							for(IDBStartupListener listener:listeners){
								listener.initialize();
							}
							monitor.done();
							UIUtility.syncExecInUIThread(new Runnable() {
								
								@Override
								public void run() {
									ss.setText("Database: "+ORMPlugin.getDefault().getDataBaseTypeName());
									
								}
							});
							
					    } 
					});
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
			}
			
		});
	}
}
