package ch.jester.system.pairing.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

import org.junit.Before;
import org.junit.Test;

import ch.jester.common.settings.SettingHelper;
import ch.jester.common.test.internal.ActivatorProviderForTestCase;
import ch.jester.common.utility.ExceptionUtility;
import ch.jester.commonservices.util.ServiceUtility;
import ch.jester.model.Category;
import ch.jester.model.Pairing;
import ch.jester.model.Player;
import ch.jester.model.PlayerCard;
import ch.jester.model.RankingSystem;
import ch.jester.model.SettingItem;
import ch.jester.model.Tournament;
import ch.jester.model.factories.ModelFactory;
import ch.jester.orm.ORMPlugin;
import ch.jester.system.api.pairing.IPairingAlgorithm;
import ch.jester.system.api.pairing.IPairingAlgorithmEntry;
import ch.jester.system.api.pairing.IPairingManager;
import ch.jester.system.exceptions.NotAllResultsException;
import ch.jester.system.exceptions.PairingNotPossibleException;
import ch.jester.system.ranking.test.BuchholzTest;
import ch.jester.system.vollrundig.RoundRobinSettings;

/**
 * Testen des Vollrundigen Paarungsalgorithmus
 * @author Peter
 *
 */
public class RoundRobinTest extends ActivatorProviderForTestCase {

	public static final String ALGORITHM_CLASS = "ch.jester.system.vollrundig.VollrundigPairingAlgorithm";
	public static final String PAIRING_PLUGIN = "ch.jester.system.vollrundig";
	private ServiceUtility mServiceUtil = new ServiceUtility();
	private IPairingAlgorithm pairingAlgorithm;
	private Tournament tournament;
	private Category cat1, cat2, cat3;
	private List<Player> players = new ArrayList<Player>();
	
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
			tournament = (Tournament)entityManager.createQuery("select t from Tournament t where t.name = 'RoundRobinTestTournament'").getSingleResult();
			cat1 = tournament.getCategories().get(0);
			cat2 = tournament.getCategories().get(1);
			cat3 = tournament.getCategories().get(2);
		} catch(NoResultException e) {
			tournament = modelFactory.createTournament("RoundRobinTestTournament");
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
	}
	
	/**
	 * Versuch zu Paaren ohne dass Spieler zur Kategorie zugeteilt wurden
	 * @throws PairingNotPossibleException
	 */
	@Test(expected=PairingNotPossibleException.class)
	public void testPairingNotPossibleException() throws PairingNotPossibleException {
		try {
			pairingAlgorithm.executePairings(tournament);
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
			PlayerCard playerCard = modelFactory.createPlayerCard(cat1, player);
			entityManager.persist(playerCard);
			cat1.addPlayerCard(playerCard);
		}
		entityManager.joinTransaction();
		entityManager.merge(tournament);

		try {
			List<Pairing> pairings = pairingAlgorithm.executePairings(cat1);
			assertEquals(6, pairings.size());
		} catch (NotAllResultsException e) {
			fail();
		} catch (PairingNotPossibleException e) {
			fail();
		} catch (Exception e) {
			fail();
		}
	}
	
	@Test
	public void testExecutePairingCategory2DoubleRounded() {
		ModelFactory modelFactory = ModelFactory.getInstance();
		RoundRobinSettings settings = new RoundRobinSettings();
		settings.setDoubleRounded(Boolean.TRUE);
		SettingHelper<RoundRobinSettings> settingHelper = new SettingHelper<RoundRobinSettings>();
		SettingItem settingItem = modelFactory.createSettingItem(tournament);
		settingItem = settingHelper.analyzeSettingObjectToStore(settings, settingItem);
		entityManager.persist(settingItem);
		
		for (Player player : players) {
			PlayerCard playerCard = modelFactory.createPlayerCard(cat2, player);
			entityManager.persist(playerCard);
			cat2.addPlayerCard(playerCard);
		}
		entityManager.joinTransaction();
		entityManager.merge(tournament);
		try {
			List<Pairing> pairings = pairingAlgorithm.executePairings(cat2);
			assertEquals(12, pairings.size());
		} catch (NotAllResultsException e) {
			fail();
		} catch (PairingNotPossibleException e) {
			fail();
		} catch (Exception e) {
			fail();
		}
	}
	
	@Test
	public void testOddNumberOfPlayers() {
		ModelFactory modelFactory = ModelFactory.getInstance();
		Player newPlayer = modelFactory.createPlayer("Gari", "Kasparov");
		entityManager.persist(newPlayer);
		players.add(newPlayer);	// 5. Spieler
		for (Player player : players) {
			PlayerCard playerCard = modelFactory.createPlayerCard(cat3, player);
			entityManager.persist(playerCard);
			cat3.addPlayerCard(playerCard);
		}
		entityManager.joinTransaction();
		entityManager.merge(tournament);
		try {
			List<Pairing> pairings = pairingAlgorithm.executePairings(cat3);
			assertEquals(30, pairings.size()); // SettingItem ist noch auf Doppelrundig gestellt
		} catch (NotAllResultsException e) {
			fail();
		} catch (PairingNotPossibleException e) {
			fail();
		} catch (Exception e) {
			fail();
		}
	}
}
