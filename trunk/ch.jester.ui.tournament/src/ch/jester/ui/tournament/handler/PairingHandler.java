package ch.jester.ui.tournament.handler;

import java.util.List;

import messages.Messages;

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
import ch.jester.commonservices.api.persistency.IDaoService;
import ch.jester.commonservices.util.ServiceUtility;
import ch.jester.model.Category;
import ch.jester.model.Tournament;
import ch.jester.system.api.pairing.IPairingAlgorithm;
import ch.jester.system.api.pairing.IPairingAlgorithmEntry;
import ch.jester.system.api.pairing.IPairingManager;
import ch.jester.system.exceptions.NoPlayersException;
import ch.jester.system.exceptions.NoRoundsException;
import ch.jester.system.exceptions.NotAllResultsException;
import ch.jester.system.exceptions.PairingNotPossibleException;
import ch.jester.system.exceptions.TournamentFinishedException;
import ch.jester.ui.tournament.cnf.TournamentNavigator;

/**
 * Handler zum Erzeugen von Paarungen
 * 
 * 
 */
public class PairingHandler extends AbstractCommandHandler implements IHandler {

	private IPairingAlgorithm pairingAlgorithm;
	private ServiceUtility mServiceUtil = new ServiceUtility();

	@Override
	public Object executeInternal(ExecutionEvent event) {
		final Category cat = getFirstSelectedAs(Category.class);
		final Tournament tournament;
		final boolean tournamentSelected;
		if (cat == null) {
			tournament = getFirstSelectedAs(Tournament.class);
			tournamentSelected = true;
		} else {
			tournament = cat.getTournament();
			tournamentSelected = false;
		}

		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindow(event);
		final Shell shell = window.getShell();
		Job job = new Job("Pairing") { //$NON-NLS-1$

			@Override
			protected IStatus run(IProgressMonitor monitor) {
				try {

					if (tournamentSelected) {
						for (Category category : tournament.getCategories()) {
							// Prüfen ob es bereits Paarungen gibt
//							if (category.getRounds().size() > 0 && category.getRounds().get(0).getPairings().size() > 0) {
//								boolean continuePairing = showWarningAlreadyPaired(shell);
//								if (!continuePairing) {
//									return Status.CANCEL_STATUS;
//								} else {
//									// Löschen der bereits erzeugten Paarungen
//									resetPairings(category);
//								}
//							}
							createPairings(category, tournament);
							
						}
					} else {
//						if (cat.getRounds().size() > 0 && cat.getRounds().get(0).getPairings().size() > 0) {
//							if (!showWarningAlreadyPaired(shell)) {
//								return Status.CANCEL_STATUS;
//							} else {
//								resetPairings(cat);
//							}
//						}
						createPairings(cat, tournament);
						
					}
				} catch (Exception e) {
					Throwable exception = ExceptionUtility.getRealException(e);
					if (exception != e) {
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
						} else if (exception instanceof NoRoundsException) {
							final String messageTitel = Messages.PairingHandler_msg_NoRounds_title;
							final String errorMessage = Messages.PairingHandler_msg_NoRounds_text;
							showErrorDialog(shell, messageTitel, errorMessage);
							return Status.CANCEL_STATUS;
						} else if (exception instanceof NoPlayersException) {
							final String messageTitel = Messages.PairingHandler_msg_NoPlayers_title;
							final String errorMessage = Messages.PairingHandler_msg_NoPlayers_text;
							showErrorDialog(shell, messageTitel, errorMessage);
							return Status.CANCEL_STATUS;
						} else if (exception instanceof TournamentFinishedException) {
							final String messageTitel = Messages.PairingHandler_msg_TournamentFinished_title;
							final String infoMessage = Messages.PairingHandler_msg_TournamentFinished_text;
							showInfoDialog(shell, messageTitel, infoMessage);
							return Status.CANCEL_STATUS;
						} else {
							showErrorDialog(shell,
									Messages.PairingHandler_msg_UnknownError_title,
									Messages.PairingHandler_msg_UnknownError_text);
							mLogger.error(exception);
							return Status.CANCEL_STATUS;
						}
					} else {
						showErrorDialog(shell,
								Messages.PairingHandler_msg_UnknownError_title,
								Messages.PairingHandler_msg_UnknownError_text);
						mLogger.error(e);
						return Status.CANCEL_STATUS;
					}
				}
				// Jetzt noch das Ganze speichern
				IDaoService<Tournament> tournamentPersister = mServiceUtil.getDaoServiceByEntity(Tournament.class);
				tournamentPersister.save(tournament);
				return Status.OK_STATUS;
			}

			private void showErrorDialog(final Shell shell, final String messageTitel, final String errorMessage) {
				UIJob uiJob = new UIJob("Error-Message") { //$NON-NLS-1$

					@Override
					public IStatus runInUIThread(IProgressMonitor monitor) {
						MessageDialog.openError(shell, messageTitel,
								errorMessage);
						return Status.OK_STATUS;
					}
				};
				uiJob.schedule();
			}

			private void showInfoDialog(final Shell shell, final String messageTitel, final String infoMessage) {
				UIJob uiJob = new UIJob("Error-Message") { //$NON-NLS-1$

					@Override
					public IStatus runInUIThread(IProgressMonitor monitor) {
						MessageDialog.openInformation(shell, messageTitel,
								infoMessage);
						return Status.OK_STATUS;
					}
				};
				uiJob.schedule();
			}
		};
		job.schedule();

		// Aktualisieren des CommonNavigators
		CommonNavigator cn = (CommonNavigator) getView(TournamentNavigator.ID);
		cn.getCommonViewer().refresh();
		return null;
	}

	/**
	 * Erzeugen der Paarungen je nach Paarungssystem.
	 * 
	 * @param cat			Kategorie zu der die Paarungen gemacht werden sollen
	 * @param tournament	Turnier (das Paarungssystem ist hier verfügbar)
	 * @throws Exception	Es können verschiedene Exceptions geworfen werden (z.B. wenn noch nicht alle Resultate vorhanden sind).
	 */
	private void createPairings(final Category cat, Tournament tournament) throws Exception {
		// Zunächst die implementierende Klasse besorgen
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
		// Paarungen ausführen
		pairingAlgorithm.executePairings(cat);
		tournament.setStarted(true);

	}
}
