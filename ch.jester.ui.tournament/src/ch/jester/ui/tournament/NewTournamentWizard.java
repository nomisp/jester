package ch.jester.ui.tournament;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;

import ch.jester.commonservices.api.logging.ILogger;
import ch.jester.commonservices.util.ServiceUtility;
import ch.jester.dao.IPersister;
import ch.jester.dao.IPlayerPersister;
import ch.jester.model.Tournament;
import ch.jester.model.factories.ModelFactory;
import ch.jester.ui.tournament.internal.Activator;

public class NewTournamentWizard extends Wizard implements INewWizard {
	
	private ILogger mLogger;
	private NewTournamentWizPageName newTournament;
	private ServiceUtility su = new ServiceUtility();
	
	public NewTournamentWizard() {
		mLogger = Activator.getDefault().getActivationContext().getLogger();
		mLogger.info("New tournament wizard started");
	}
	
	@Override
	public void addPages() {
		mLogger.info("New tournament wizard started: adding pages");
		super.addPages();
		addPage(newTournament);
	}

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		setWindowTitle("New Tournament");
		newTournament = new NewTournamentWizPageName();
	}

	@Override
	public boolean performFinish() {
		
		try {
			this.getContainer().run(false, false, new IRunnableWithProgress() {
				
				@Override
				public void run(IProgressMonitor monitor) throws InvocationTargetException,
						InterruptedException {
					String tournamentName = newTournament.getTournamentName().getText();
					ModelFactory mf = ModelFactory.getInstance();
					mLogger.debug("Creating tournament: " + tournamentName);
					Tournament tournament = mf.createTournament(tournamentName);
					// TODO Peter: Persistieren
//					IPersister<Tournament> tournamentPersister = su.getExclusiveService(IPersister.class);
//					tournamentPersister.save(tournament);
					
					mLogger.debug("Tournament " + tournament.getName() + " with Id " + tournament.getId() + " created");
				}
			});
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		return true;
	}

}
