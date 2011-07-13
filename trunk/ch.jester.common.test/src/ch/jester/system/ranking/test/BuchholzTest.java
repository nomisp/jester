package ch.jester.system.ranking.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import org.junit.Before;
import org.junit.Test;

import ch.jester.common.test.internal.ActivatorProviderForTestCase;
import ch.jester.commonservices.util.ServiceUtility;
import ch.jester.model.Category;
import ch.jester.model.FinalRanking;
import ch.jester.model.Pairing;
import ch.jester.model.Player;
import ch.jester.model.PlayerCard;
import ch.jester.model.Ranking;
import ch.jester.model.RankingEntry;
import ch.jester.model.RankingSystem;
import ch.jester.model.RankingSystemPoint;
import ch.jester.model.Round;
import ch.jester.model.Tournament;
import ch.jester.model.factories.ModelFactory;
import ch.jester.orm.ORMPlugin;
import ch.jester.system.api.ranking.IRankingSystem;
import ch.jester.system.api.ranking.IRankingSystemEntry;
import ch.jester.system.api.ranking.IRankingSystemManager;
import ch.jester.system.exceptions.NotAllResultsException;
import ch.jester.system.pairing.test.RoundRobinTest;

public class BuchholzTest extends ActivatorProviderForTestCase {
	public static final String PLUGIN_ID = "ch.jester.rankingsystem.buchholz";
	public static final String RANKINGSYSTEM_CLASS = "ch.jester.rankingsystem.buchholz.BuchholzRankingSystem";
	public static final String RANKINGSYSTEM_TYPE = "Buchholz";
	private ServiceUtility mServiceUtil = new ServiceUtility();
	private IRankingSystem buchholzSystem;
	private RankingSystem rankingSystem;
	private Tournament t;
	private Category cat;
	private EntityManager entityManager;
	
	@Before
	public void setUp() {
		IRankingSystemManager rankingSystemManager = mServiceUtil.getService(IRankingSystemManager.class);
		List<IRankingSystemEntry> registredEntries = rankingSystemManager.getRegistredEntries();
		for (IRankingSystemEntry rankingSystemEntry : registredEntries) {
			if (rankingSystemEntry.getImplementationClass().equals(RANKINGSYSTEM_CLASS)) {
				buchholzSystem = rankingSystemEntry.getService();
				break;
			}
		}

		rankingSystem = new RankingSystem();
		rankingSystem.setPluginId(PLUGIN_ID);
		rankingSystem.setImplementationClass(RANKINGSYSTEM_CLASS);
		rankingSystem.setShortType(RANKINGSYSTEM_TYPE);
		rankingSystem.setRankingSystemNumber(1);
		
		t = new Tournament();
		t.setName("TestBuchholzTournament");
		t.addRankingSystem(rankingSystem);
		t.setPairingSystemPlugin(RoundRobinTest.PAIRING_PLUGIN);
		t.setPairingSystem(RoundRobinTest.ALGORITHM_CLASS);
		t.setEloCalculator("ch.jester.system.fidecalculator.FideCalculator");
		cat = new Category();
		cat.setDescription("HT1");
		cat.setPlayerCards(initPlayerCards());
		initPairings(cat);
		t.addCategory(cat);
		entityManager = ORMPlugin.getJPAEntityManager();
		if (entityManager.getTransaction().isActive()) {
			entityManager.joinTransaction();
		} else {
			entityManager.getTransaction().begin();
		}
		entityManager.clear();
		entityManager.persist(t);
		entityManager.flush();
	}
	
	@Test
	public void testExecuteRankingDifferentNrOfPoints() {
		List<Round> rounds = cat.getRounds();
		Round round = rounds.get(0);
		List<Pairing> pairings = round.getPairings();
		pairings.get(0).setResult("1");
		pairings.get(1).setResult("1");
		round = rounds.get(1);
		pairings = round.getPairings();
		pairings.get(0).setResult("0");
		pairings.get(1).setResult("1");
		round = rounds.get(2);
		pairings = round.getPairings();
		pairings.get(0).setResult("1");
		pairings.get(1).setResult("0");
		entityManager.joinTransaction();
		entityManager.persist(t);
		entityManager.flush();
		try {
			Ranking ranking = buchholzSystem.calculateRanking(cat, null);
			assertTrue(ranking instanceof FinalRanking);
			
			List<RankingEntry> rankingEntries = ranking.getRankingEntries();
			assertTrue(rankingEntries.size()>0);
			for (int i = 0; i < rankingEntries.size(); i++) {
				assertEquals(rankingEntries.get(i).getPlayerCard(), cat.getPlayerCards().get(i));
			}
			
		} catch (NotAllResultsException e) {
			e.printStackTrace();
		}
	}
	
	private List<PlayerCard> initPlayerCards() {
		List<Player> players = new ArrayList<Player>();
		Player p1 = new Player();
		p1.setFirstName("P1First");
		p1.setLastName("P1Last");
		Player p2 = new Player();
		p2.setFirstName("P2First");
		p2.setLastName("P2Last");
		Player p3 = new Player();
		p3.setFirstName("P3First");
		p3.setLastName("P3Last");
		Player p4 = new Player();
		p4.setFirstName("P4First");
		p4.setLastName("P4Last");
		players.add(p1);
		players.add(p2);
		players.add(p3);
		players.add(p4);
		List<PlayerCard> playerCards = new ArrayList<PlayerCard>();
		for (Player player : players) {
			//PlayerCard playerCard = new PlayerCard();
			PlayerCard playerCard = ModelFactory.getInstance().createPlayerCard(cat, player);
			playerCard.setPlayer(player);
			playerCard.addRankingSystemPoint(new RankingSystemPoint(RANKINGSYSTEM_TYPE));
			playerCards.add(playerCard);
		}
		return playerCards;
	}
	
	private void initPairings(Category cat) {
		List<PlayerCard> playerCards = cat.getPlayerCards();
		PlayerCard p1 = playerCards.get(0);
		PlayerCard p2 = playerCards.get(1);
		PlayerCard p3 = playerCards.get(2);
		PlayerCard p4 = playerCards.get(3);
		
		Round r1 = new Round();
		r1.setNumber(1);
		Round r2 = new Round();
		r2.setNumber(2);
		Round r3 = new Round();
		r3.setNumber(3);
		Pairing pair1 = new Pairing();
		pair1.setWhite(p1);
		pair1.setBlack(p4);
		Pairing pair2 = new Pairing();
		pair2.setWhite(p2);
		pair2.setBlack(p3);
		r1.addPairing(pair1);
		r1.addPairing(pair2);
		
		Pairing pair3 = new Pairing();
		pair3.setWhite(p4);
		pair3.setBlack(p3);
		Pairing pair4 = new Pairing();
		pair4.setWhite(p1);
		pair4.setBlack(p2);
		r2.addPairing(pair3);
		r2.addPairing(pair4);
		
		Pairing pair5 = new Pairing();
		pair5.setWhite(p2);
		pair5.setBlack(p4);
		Pairing pair6 = new Pairing();
		pair6.setWhite(p3);
		pair6.setBlack(p1);
		r3.addPairing(pair5);
		r3.addPairing(pair6);
		
		cat.addRound(r1);
		cat.addRound(r2);
		cat.addRound(r3);
	}
}
