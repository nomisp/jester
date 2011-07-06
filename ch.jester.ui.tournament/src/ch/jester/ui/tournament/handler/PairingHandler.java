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
import org.eclipse.ui.navigator.CommonNavigator;
import org.eclipse.ui.progress.UIJob;

import ch.jester.common.ui.handlers.AbstractCommandHandler;
import ch.jester.common.utility.ExceptionUtility;
import ch.jester.commonservices.util.ServiceUtility;
import ch.jester.model.Category;
import ch.jester.model.Tournament;
import ch.jester.system.api.pairing.IPairingAlgorithm;
import ch.jester.system.api.pairing.IPairingAlgorithmEntry;
import ch.jester.system.api.pairing.IPairingManager;
import ch.jester.system.exceptions.NotAllResultsException;
import ch.jester.system.exceptions.PairingNotPossibleException;
import ch.jester.ui.tournament.cnf.TournamentNavigator;
import ch.jester.ui.tournament.nl1.Messages;

/**
 * Handler zum Erzeugen von Paarungen
 * @author Peter
 *
 */
public class PairingHandler extends AbstractCommandHandler implements IHandler {
	
	private IPairingAlgorithm pairingAlgorithm;

	@Override
	public Object executeInternal(ExecutionEvent event) {
		//MessageDialog.openInformation(HandlerUtil.getActiveWorkbenchWindow(event).getShell(), "ExecutePairing", "Execute pairings");
		final Category cat = getFirstSelectedAs(Category.class);
		Tournament tournament = null;
		if (cat == null) {
			tournament = getFirstSelectedAs(Tournament.class);
		} else {
			tournament = cat.getTournament();
		}
		String pairingSystemClass = tournament.getPairingSystem();
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
		Job job = new Job("Pairing") { //$NON-NLS-1$
			
			@Override
			protected IStatus run(IProgressMonitor monitor) {
				try {
					pairingAlgorithm.executePairings(cat, monitor);
				} catch (Exception e) {
					Throwable exception = ExceptionUtility.getRealException(e);
					if (exception != e) {
						// TODO Peter: Fehlermeldung übersetzen!
						if (exception instanceof PairingNotPossibleException) {
							final String messageTitel = Messages.PairingHandler_msg_PairingNotPossible_title;
							final String errorMessage = Messages.PairingHandler_msg_PairingNotPossible_text;
							showErrorDialog(shell, messageTitel, errorMessage);
							return Status.CANCEL_STATUS;
						} else if (exception instanceof NotAllResultsException) {
							final String messageTitel = Messages.PairingHandler_msg_NotAllResults_title;
							final String errorMessage = Messages.PairingHandler_msg_NotAllResults_text;
							showErrorDialog(shell, messageTitel, errorMessage);
							return Status.CANCEL_STATUS;
						} else {
							showErrorDialog(shell, Messages.PairingHandler_msg_UnknownError_title, Messages.PairingHandler_msg_UnknownError_text);
							mLogger.error(e);
							return Status.CANCEL_STATUS;
						}
					} else {
						return Status.CANCEL_STATUS;
					}
				}
				return Status.OK_STATUS;
			}

			private void showErrorDialog(final Shell shell,
					final String messageTitel, final String errorMessage) {
				UIJob uiJob = new UIJob("Error-Message") { //$NON-NLS-1$
					
					@Override
					public IStatus runInUIThread(IProgressMonitor monitor) {
						MessageDialog.openError(shell, messageTitel, errorMessage);
						return Status.OK_STATUS;
					}
				};
				uiJob.schedule();
			}
		};
		job.schedule();
		// Aktualisieren des CommonNavigators
		CommonNavigator cn = (CommonNavigator)getView(TournamentNavigator.ID);
		cn.getCommonViewer().refresh();
		return null;
	}
}
