package ch.jester.ui.tournament.handler;

import java.util.ArrayList;
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
import ch.jester.model.RankingSystem;
import ch.jester.model.Tournament;
import ch.jester.system.api.ranking.IRankingSystem;
import ch.jester.system.api.ranking.IRankingSystemEntry;
import ch.jester.system.api.ranking.IRankingSystemManager;
import ch.jester.system.exceptions.NotAllResultsException;
import ch.jester.ui.tournament.cnf.TournamentNavigator;

public class RankingHandler extends AbstractCommandHandler implements IHandler {

	private List<IRankingSystem> rankingSystems = new ArrayList<IRankingSystem>();
	
	@Override
	public Object executeInternal(ExecutionEvent event) {
		final Category cat = getFirstSelectedAs(Category.class);
		final Tournament tournament;
		if (cat == null) {
			tournament = getFirstSelectedAs(Tournament.class);
		} else {
			tournament = cat.getTournament();
		}
		List<RankingSystem> rankingSystemClasses = tournament.getRankingSystems();
		ServiceUtility su = new ServiceUtility();
		IRankingSystemManager rankingSystemManager = su.getService(IRankingSystemManager.class);
		List<IRankingSystemEntry> registredEntries = rankingSystemManager.getRegistredEntries();
		for (IRankingSystemEntry rankingSystemEntry : registredEntries) {
			for (RankingSystem rankingSystemClass : rankingSystemClasses) {
				if (rankingSystemEntry.getImplementationClass().equals(rankingSystemClass)) {
					rankingSystems.add(rankingSystemEntry.getService());
				}
			}
		}
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindow(event);
		final Shell shell = window.getShell();
		Job job = new Job("Ranking") {
			
			@Override
			protected IStatus run(IProgressMonitor monitor) {
				try {
					if (cat == null) {
						rankingSystems.get(0).calculateRanking(tournament, monitor);	// TODO Peter: Evtl. für kommende Version mehrere Feinwertungen
					} else {
						rankingSystems.get(0).calculateRanking(cat, monitor);	// TODO Peter: Evtl. für kommende Version mehrere Feinwertungen
					}
					
				} catch (Exception e) {
					Throwable exception = ExceptionUtility.getRealException(e);
					if (exception != e) {
						if (exception instanceof NotAllResultsException) {
							final String messageTitel = "Not all Results";
							final String errorMessage = "There are not all results available from the last round!";
							showErrorDialog(shell, messageTitel, errorMessage);
						}
					} else {
						return Status.CANCEL_STATUS;
					}
				}
				return Status.OK_STATUS;
			}
			
			private void showErrorDialog(final Shell shell,
					final String messageTitel, final String errorMessage) {
				UIJob uiJob = new UIJob("Error-Message") {
					
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
