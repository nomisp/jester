package ch.jester.system.pairing.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

import org.junit.Before;
import org.junit.Test;

import ch.jester.common.test.internal.ActivatorProviderForTestCase;
import ch.jester.common.utility.ExceptionUtility;
import ch.jester.commonservices.util.ServiceUtility;
import ch.jester.model.Category;
import ch.jester.model.Pairing;
import ch.jester.model.Player;
import ch.jester.model.PlayerCard;
import ch.jester.model.RankingSystem;
import ch.jester.model.Round;
import ch.jester.model.Tournament;
import ch.jester.model.factories.ModelFactory;
import ch.jester.orm.ORMPlugin;
import ch.jester.system.api.pairing.IPairingAlgorithm;
import ch.jester.system.api.pairing.IPairingAlgorithmEntry;
import ch.jester.system.api.pairing.IPairingManager;
import ch.jester.system.exceptions.NoPlayersException;
import ch.jester.system.exceptions.NoRoundsException;
import ch.jester.system.exceptions.NotAllResultsException;
import ch.jester.system.exceptions.PairingNotPossibleException;
import ch.jester.system.ranking.test.BuchholzTest;

/**
 * Testen des Vollrundigen Paarungsalgorithmus
 *
 */
public class SwissSimpleTest extends ActivatorProviderForTestCase {

	public static final String ALGORITHM_CLASS = "ch.jester.system.swiss.simple.SwissSimplePairingAlgorithm";
	public static final String PAIRING_PLUGIN = "ch.jester.system.swiss.simple";
	private ServiceUtility mServiceUtil = new ServiceUtility();
	private IPairingAlgorithm pairingAlgorithm;
	private Tournament tournament;
	private Category cat1, cat2, cat3;
	private List<Player> players = new ArrayList<Player>();	// Liste um die Spieler zu halten bevor sie persistiert sind
	private List<Round> rounds = new ArrayList<Round>();	// Liste um die Runden zu halten bevor sie persistiert sind
	
	private EntityManager entityManager;
	
	/**
	 * Vorbereiten der Testdaten
	 */
	@Before
	public void setUp() {
		entityManager = ORMPlugin.getJPAEntityManager();
		ModelFactory modelFactory = ModelFactory.getInstance();
		if (entityManager.getTransaction().isActive()) {
			entityManager.joinTransaction();
		} else {
			entityManager.getTransaction().begin();
		}
		entityManager.clear();
		
		// PairingAlgorithm besorgen
		IPairingManager pairingManager = mServiceUtil.getService(IPairingManager.class);
		List<IPairingAlgorithmEntry> registredEntries = pairingManager.getRegistredEntries();
		for (IPairingAlgorithmEntry pairingEntry : registredEntries) {
			if (pairingEntry.getImplementationClass().equals(ALGORITHM_CLASS)) {
				pairingAlgorithm = pairingEntry.getService();
				break;
			}
		}

		RankingSystem rankingSystem = new RankingSystem();
		rankingSystem.setPluginId(BuchholzTest.PLUGIN_ID);
		rankingSystem.setImplementationClass(BuchholzTest.RANKINGSYSTEM_CLASS);
		rankingSystem.setShortType(BuchholzTest.RANKINGSYSTEM_TYPE);
		rankingSystem.setRankingSystemNumber(1);
		
		try {
			tournament = (Tournament)entityManager.createQuery("select t from Tournament t where t.name = 'SwissSimpleTestTournament'").getSingleResult();
			cat1 = tournament.getCategories().get(0);
			cat2 = tournament.getCategories().get(1);
			cat3 = tournament.getCategories().get(2);
		} catch(NoResultException e) {
			tournament = modelFactory.createTournament("SwissSimpleTestTournament");
			tournament.setYear(2011);
			tournament.setPairingSystemPlugin(PAIRING_PLUGIN);
			tournament.setPairingSystem(ALGORITHM_CLASS);
			tournament.setEloCalculator("ch.jester.system.fidecalculator.FideCalculator");
			tournament.addRankingSystem(rankingSystem);
			cat1 = modelFactory.createCategory("Cat1");
			cat2 = modelFactory.createCategory("Cat2");
			cat3 = modelFactory.createCategory("Cat3");
			tournament.addCategory(cat1);
			tournament.addCategory(cat2);
			tournament.addCategory(cat3);
			
			if (tournament.isUnsafed()) {
				entityManager.persist(tournament);
			} else {
				entityManager.merge(tournament);
			}
		}
		
		if (players.isEmpty()) {
			players.add(modelFactory.createPlayer("Peter", "Simon"));
			players.add(modelFactory.createPlayer("Matthias", "Liechti"));
			players.add(modelFactory.createPlayer("Thomas", "Letsch"));
			players.add(modelFactory.createPlayer("Fredi", "Dönni"));
		}
		
		if (rounds.isEmpty()) {
			for (int i = 0; i < 3; i++) {	// Für die 3 Kategorien
				for (int j = 0; j < 3; j++) { // Jeweils 3 Runden
					rounds.add(modelFactory.createRound(j+1));
				}
			}
			
		}
	}
	
	/**
	 * Versuch zu Paaren ohne dass Spieler zur Kategorie zugeteilt wurden
	 * Test-ID: U-SP-9
	 * @throws NoRoundsException
	 */
	@Test(expected=NoRoundsException.class)
	public void testNoRoundsException() throws NoRoundsException {
		// Falls es von einem verherigen Test Runden hat diese löschen -> Problem von JUnit, dass die Reihenfolge der Ausführung nicht definiert ist
		cat1.getRounds().clear();
		cat2.getRounds().clear();
		cat3.getRounds().clear();
		try {
			pairingAlgorithm.executePairings(tournament);
		} catch (Exception e) {
			Throwable realException = ExceptionUtility.getRealException(e);
			if (realException instanceof NoRoundsException) {
				throw (NoRoundsException)realException;
			}
		}
	}
	
	/**
	 * Versuch zu Paaren ohne dass Spieler zur Kategorie zugeteilt wurden
	 * Test-ID: U-SP-10
	 * @throws NoPlayersException
	 */
	@Test(expected=NoPlayersException.class)
	public void testNoPlayersException() throws NoPlayersException {
		cat1.getPlayerCards().clear();
		if (cat1.getRounds().isEmpty() && cat2.getRounds().isEmpty() && cat3.getRounds().isEmpty()) addRoundsToCategories();
		try {
			pairingAlgorithm.executePairings(tournament);
		} catch (Exception e) {
			Throwable realException = ExceptionUtility.getRealException(e);
			if (realException instanceof NoPlayersException) {
				throw (NoPlayersException)realException;
			}
		}
	}
	
	/**
	 * Testen des Paarens der 1.Runde
	 * Test-ID: U-SP-7
	 */
	@Test
	public void testExecutePairingCategory1() {
		ModelFactory modelFactory = ModelFactory.getInstance();
		for (Player player : players) {
			PlayerCard playerCard = modelFactory.createPlayerCard(cat1, player);
			entityManager.persist(playerCard);
			cat1.addPlayerCard(playerCard);
		}
		if (cat1.getRounds().isEmpty() && cat2.getRounds().isEmpty() && cat3.getRounds().isEmpty()) addRoundsToCategories();
		entityManager.joinTransaction();
		entityManager.merge(tournament);

		try {
			List<Pairing> pairings = pairingAlgorithm.executePairings(cat1);
			assertEquals(2, pairings.size());
			Pairing p1 = pairings.get(0);
			Pairing p2 = pairings.get(1);
			assertEquals("Dönni", p1.getWhite().getPlayer().getLastName());
			assertEquals("Letsch", p1.getBlack().getPlayer().getLastName());
			assertEquals("Liechti", p2.getWhite().getPlayer().getLastName());
			assertEquals("Simon", p2.getBlack().getPlayer().getLastName());
			entityManager.persist(p1);
			entityManager.persist(p2);
		} catch (NotAllResultsException e) {
			fail();
		} catch (PairingNotPossibleException e) {
			fail();
		} catch (Exception e) {
			fail();
		}
	}

	/**
	 * Testen des Paarens der 2.Runde
	 * Test-ID: U-SP-11
	 */
	@Test
	public void testExecutePairingCategory1Round2() {
		List<Pairing> pairings = null;
		try {
			pairings = pairingAlgorithm.executePairings(cat1);
		} catch (Exception e) {
			assertTrue(ExceptionUtility.getRealException(e) instanceof NotAllResultsException);
			if (ExceptionUtility.getRealException(e) instanceof NotAllResultsException) {
				// Das Paaren der 2. Runde darf erst erfolgen, wenn die Resultate der 1. Runde erfasst wurden
				try {
					List<Pairing> playedPairings = cat1.getRounds().get(0).getPairings();
					Pairing p1 = playedPairings.get(0);
					Pairing p2 = playedPairings.get(1);
					p1.setResult("1");
					p2.setResult("0");
					pairings = pairingAlgorithm.executePairings(cat1);
					assertEquals(7, pairings.size());
					p1 = pairings.get(5);
					p2 = pairings.get(6);
					assertEquals("Simon", p1.getWhite().getPlayer().getLastName());
					assertEquals("Dönni", p1.getBlack().getPlayer().getLastName());
					assertEquals("Liechti", p2.getWhite().getPlayer().getLastName());
					assertEquals("Letsch", p2.getBlack().getPlayer().getLastName());
				} catch (Exception e1) {
					fail(); // Nun sollte es gehen
				}
			} else {
				fail();
			}
		}
	}
	
	/**
	 * Testen einer ungeraden Anzahl Spieler
	 * Test-ID: U-SP-8
	 */
	@Test
	public void testOddNumberOfPlayers() {
		ModelFactory modelFactory = ModelFactory.getInstance();
		Player newPlayer = modelFactory.createPlayer("Gari", "Kasparov");
		entityManager.persist(newPlayer);
		players.add(newPlayer);	// 5. Spieler

		for (Player player : players) {
			PlayerCard playerCard = modelFactory.createPlayerCard(cat2, player);
			entityManager.persist(playerCard);
			cat2.addPlayerCard(playerCard);
		}
		if (cat1.getRounds().isEmpty() && cat2.getRounds().isEmpty() && cat3.getRounds().isEmpty()) addRoundsToCategories();
		entityManager.joinTransaction();
		entityManager.merge(tournament);
		try {
			List<Pairing> pairings = pairingAlgorithm.executePairings(cat2);
			assertEquals(5, pairings.size());
			Pairing p1 = pairings.get(2);
			Pairing p2 = pairings.get(3);
			Pairing p3 = pairings.get(4);
			assertEquals("Dönni", p1.getWhite().getPlayer().getLastName());
			assertEquals("Kasparov", p1.getBlack().getPlayer().getLastName());
			assertEquals("Letsch", p2.getWhite().getPlayer().getLastName());
			assertEquals("Liechti", p2.getBlack().getPlayer().getLastName());
			assertEquals("Simon", p3.getWhite().getPlayer().getLastName());
			assertNull(p3.getBlack());
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * Hinzufügen der Runden zu den 3 Kategorien
	 */
	private void addRoundsToCategories() {
		for (int i = 0; i < rounds.size(); i++) {
			Round round = rounds.get(i);
			entityManager.persist(round);
			if (i < 3) {
				cat1.addRound(round);
			} else if (i > 2 && i < 6) {
				cat2.addRound(round);
			} else if (i > 5) {
				cat3.addRound(round);
			}
		}
	}
}
