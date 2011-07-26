package ch.jester.ui.tournament;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import messages.Messages;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;

import ch.jester.commonservices.api.logging.ILogger;
import ch.jester.commonservices.api.persistency.IDaoService;
import ch.jester.commonservices.util.ServiceUtility;
import ch.jester.model.Category;
import ch.jester.model.RankingSystem;
import ch.jester.model.Tournament;
import ch.jester.model.factories.ModelFactory;
import ch.jester.ui.tournament.internal.Activator;

public class NewTournamentWizard extends Wizard implements INewWizard {
	
	private ILogger mLogger;
	private NewTournamentWizPageName newTournament;
	private NewTournWizPageSystem systemPage;
	private NewTournWizPageCategories categoriesPage;
	private ServiceUtility su = new ServiceUtility();
	
	public NewTournamentWizard() {
		mLogger = Activator.getDefault().getActivationContext().getLogger();
		mLogger.info("New tournament wizard started"); //$NON-NLS-1$
	}
	
	@Override
	public void addPages() {
		mLogger.info("New tournament wizard started: adding pages"); //$NON-NLS-1$
		super.addPages();
		addPage(newTournament);
		addPage(systemPage);
		addPage(categoriesPage);
	}

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		setWindowTitle(Messages.NewTournamentWizard_NewTournament);
		newTournament = new NewTournamentWizPageName();
		systemPage = new NewTournWizPageSystem();
		categoriesPage = new NewTournWizPageCategories();
	}
	
	@Override
	public boolean performFinish() {
		
		try {
			this.getContainer().run(false, false, new IRunnableWithProgress() {
				
				@Override
				public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
					// Kategorien holen und persistieren
					List<Category> categories = getCategories();
//					ICategoryDao categoryPersister = su.getExclusiveService(ICategoryDao.class);
//					categoryPersister.save(categories);
					
					String tournamentName = newTournament.getTournamentName();
					String description = newTournament.getDescription();
					Date dateFrom = newTournament.getDateFrom();
					Date dateTo = newTournament.getDateTo();
					
					ModelFactory mf = ModelFactory.getInstance();
					mLogger.debug("Creating tournament: " + tournamentName); //$NON-NLS-1$
					Tournament tournament = mf.createTournament(tournamentName);
					tournament.setDescription(description);
					tournament.setYear(getYear(dateFrom));
					tournament.setDateFrom(dateFrom);
					tournament.setDateTo(dateTo);
					tournament.setPairingSystemPlugin(systemPage.getPairingAlgorithmEntry().getPluginId());
					tournament.setPairingSystem(systemPage.getPairingAlgorithmEntry().getImplementationClass());
					tournament.setSettingsPage(systemPage.getPairingAlgorithmEntry().getSettingsPage());
//					tournament.setRankingSystem(systemPage.getRankingSystemEntry().getImplementationClass());
//					tournament.setRankingSystemType(systemPage.getRankingSystemEntry().getShortType());
					tournament.setEloCalculator(systemPage.getEloCalculatorEntry().getImplementationClass());
					tournament.setCategories(categories);	// Aufgrund des Cascadings werden die hinzugefügten Kategorien gleich mit persistiert
					tournament.setActive(true);
					tournament.setStarted(false);
					RankingSystem rs = mf.createRankingSystem(tournament, 
							systemPage.getRankingSystemEntry().getPluginId(), 
							systemPage.getRankingSystemEntry().getImplementationClass(), 
							systemPage.getRankingSystemEntry().getShortType(),
							systemPage.getRankingSystemEntry().getDescription());
					tournament.addRankingSystem(rs);
					IDaoService<Tournament> tournamentPersister = su.getDaoServiceByEntity(Tournament.class);//su.getExclusiveService(ITournamentDao.class);
					tournamentPersister.save(tournament);
//					categoryPersister.save(categories);
					mLogger.debug("Tournament " + tournament.getName() + " with Id " + tournament.getId() + " created"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				}
			});
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		return true;
	}
	
	private int getYear(Date date) {
		Calendar cal = Calendar.getInstance();
		if (date == null) date = new Date();
		cal.setTime(date);
		return cal.get(Calendar.YEAR);
	}
	
	/**
	 * Holen der Kategorien von der WizardPage
	 * @return
	 */
	private List<Category> getCategories() {
		ModelFactory mf = ModelFactory.getInstance();
		List<Category> categories = new ArrayList<Category>();
		categories.addAll(categoriesPage.getCategories());
		for (Category category : categories) {
			for (int i = 0; i < category.getMaxRounds(); i++) {
				category.addRound(mf.createRound(i+1));
			}
		}
		return categories;
	}
}
