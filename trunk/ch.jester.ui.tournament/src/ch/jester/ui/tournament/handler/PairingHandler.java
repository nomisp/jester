package ch.jester.ui.tournament.handler;

import java.util.List;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.progress.UIJob;

import ch.jester.common.ui.handlers.AbstractCommandHandler;
import ch.jester.commonservices.util.ServiceUtility;
import ch.jester.model.Category;
import ch.jester.system.api.pairing.IPairingAlgorithm;
import ch.jester.system.api.pairing.IPairingAlgorithmEntry;
import ch.jester.system.api.pairing.IPairingManager;
import ch.jester.system.exceptions.NotAllResultsException;

public class PairingHandler extends AbstractCommandHandler implements IHandler {
	
	private IPairingAlgorithm pairingAlgorithm;

	@Override
	public Object executeInternal(ExecutionEvent event) {
		MessageDialog.openInformation(HandlerUtil.getActiveWorkbenchWindow(event).getShell(), "ExecutePairing", "Execute pairings");
		final Category cat = getFirstSelectedAs(Category.class);
		String pairingSystemClass = cat.getTournament().getPairingSystem();
		ServiceUtility su = new ServiceUtility();
		IPairingManager pairingManager = su.getService(IPairingManager.class);
		List<IPairingAlgorithmEntry> registredEntries = pairingManager.getRegistredEntries();
		for (IPairingAlgorithmEntry pairingEntry : registredEntries) {
			if (pairingEntry.getImplementationClass().equals(pairingSystemClass)) {
				pairingAlgorithm = pairingEntry.getService();
				break;
			}
		}
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindow(event);
		final Shell shell = window.getShell();
		Job job = new Job("Pairing") {
			
			@Override
			protected IStatus run(IProgressMonitor monitor) {
				try {
					pairingAlgorithm.executePairings(cat, monitor);
				} catch (Exception e) {
					// TODO Peter: Fehlermeldung übersetzen!
					UIJob uiJob = new UIJob("Error-Message") {
						
						@Override
						public IStatus runInUIThread(IProgressMonitor monitor) {
							MessageDialog.openError(shell, "Not all Results", "There are not all results available from the last round!");
							return Status.OK_STATUS;
						}
					};
					uiJob.schedule();
				}
				return Status.OK_STATUS;
			}
		};
		job.schedule();
		return null;
	}

}
