package ch.jester.system.pairing.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.junit.Before;
import org.junit.Test;

import ch.jester.common.test.internal.ActivatorProviderForTestCase;
import ch.jester.common.utility.ExceptionUtility;
import ch.jester.commonservices.util.ServiceUtility;
import ch.jester.model.Category;
import ch.jester.model.Pairing;
import ch.jester.model.Player;
import ch.jester.model.Tournament;
import ch.jester.model.factories.ModelFactory;
import ch.jester.system.api.pairing.IPairingAlgorithm;
import ch.jester.system.api.pairing.IPairingAlgorithmEntry;
import ch.jester.system.api.pairing.IPairingManager;
import ch.jester.system.exceptions.NotAllResultsException;
import ch.jester.system.exceptions.PairingNotPossibleException;

public class RoundRobinTest extends ActivatorProviderForTestCase {

	private static final String ALGORITHM_CLASS = "ch.jester.system.vollrundig.VollrundigPairingAlgorithm";
	private ServiceUtility mServiceUtil = new ServiceUtility();
	private IPairingAlgorithm pairingAlgorithm;
	private Tournament tournament;
	private Category cat1, cat2, cat3;
	private List<Player> players = new ArrayList<Player>();
	
	@Before
	public void setUp() {
		IPairingManager pairingManager = mServiceUtil.getService(IPairingManager.class);
		List<IPairingAlgorithmEntry> registredEntries = pairingManager.getRegistredEntries();
		for (IPairingAlgorithmEntry pairingEntry : registredEntries) {
			if (pairingEntry.getImplementationClass().equals(ALGORITHM_CLASS)) {
				pairingAlgorithm = pairingEntry.getService();
				break;
			}
		}
		createData();
//		IDaoService<Tournament> tournamentPersister = mServiceUtil.getDaoService(Tournament.class);
//		tournamentPersister.save(tournament);
	}
	
//	@AfterClass
//	public void tearDown() {
//		
//	}
	
	private void createData() {
		ModelFactory modelFactory = ModelFactory.getInstance();
		tournament = modelFactory.createTournament("TestTournament");
		tournament.setYear(2011);
		tournament.setPairingSystem(ALGORITHM_CLASS);
		tournament.setEloCalculator("ch.jester.system.fidecalculator.FideCalculator");
		tournament.setRankingSystem("ch.jester.rankingsystem.buchholz.BuchholzRankingSystem");
		cat1 = modelFactory.createCategory("Cat1");
		cat2 = modelFactory.createCategory("Cat2");
		cat3 = modelFactory.createCategory("Cat3");
		tournament.addCategory(cat1);
		tournament.addCategory(cat2);
		tournament.addCategory(cat3);
		
		players.add(modelFactory.createPlayer("Peter", "Simon"));
		players.add(modelFactory.createPlayer("Matthias", "Liechti"));
		players.add(modelFactory.createPlayer("Thomas", "Letsch"));
		players.add(modelFactory.createPlayer("Fredi", "Dönni"));
	}
	
	/**
	 * Versuch zu Paaren ohne dass Spieler zur Kategorie zugeteilt wurden
	 * @throws PairingNotPossibleException
	 */
	@Test(expected=PairingNotPossibleException.class)
	public void testPairingNotPossibleException() throws PairingNotPossibleException {
		try {
			pairingAlgorithm.executePairings(tournament, null);
		} catch (Exception e) {
			Throwable realException = ExceptionUtility.getRealException(e);
			if (realException instanceof PairingNotPossibleException) {
				throw (PairingNotPossibleException)realException;
			}
		}
	}
	
	@Test
	public void testExecutePairingCategory1() {
		ModelFactory modelFactory = ModelFactory.getInstance();
		for (Player player : players) {
			cat1.addPlayerCard(modelFactory.createPlayerCard(cat1, player));
		}
		Job job = new Job("Pairing") {
			
			@Override
			protected IStatus run(IProgressMonitor monitor) {
	
				try {
					List<Pairing> pairings = pairingAlgorithm.executePairings(cat1, null);
					assertEquals(6, pairings.size());
				} catch (NotAllResultsException e) {
					fail();
				} catch (PairingNotPossibleException e) {
					fail();
				} catch (Exception e) {
					fail();
				}
				return Status.OK_STATUS;
			}
		};
		job.schedule();
	}
}