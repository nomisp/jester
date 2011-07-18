package ch.jester.common.tests;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.util.LocalSelectionTransfer;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.dnd.Clipboard;
import org.junit.Assert;
import org.junit.Test;

import ch.jester.common.ui.handlers.CloneUtility;
import ch.jester.common.ui.utility.GlobalClipBoard;
import ch.jester.commonservices.api.persistency.IEntityObject;
import ch.jester.commonservices.api.reportengine.IReportEngine;
import ch.jester.commonservices.util.ServiceUtility;
import ch.jester.model.Category;
import ch.jester.model.Pairing;
import ch.jester.model.Player;
import ch.jester.model.PlayerCard;
import ch.jester.model.Round;
import ch.jester.model.Tournament;
import ch.jester.model.factories.ModelFactory;
import ch.jester.system.pairing.test.RoundRobinTest;

public class CopyPasteTest{

	
	private ServiceUtility su = new ServiceUtility();

	static Tournament clone;

	@Test
	public void testCopyTournament(){
		List<Tournament> t;
		su.getDaoServiceByEntity(Tournament.class).save(t = getDummyInput(Tournament.class));
		Tournament tournament = t.get(0);
		Exception ex=null;
		try{
		clone = (Tournament) tournament.clone();
		}catch(Exception e){
			ex = e;
		}
		Assert.assertTrue(ex==null);
	}
	@Test
	public void testPasteTournament(){
		List<Tournament> t;
		su.getDaoServiceByEntity(Tournament.class).save(clone);

	}
	
	
	private <T extends IEntityObject>List<T> getDummyInput(Class<T> clz) {
		Tournament t = new Tournament();
		t.setName("SonnebornBergerTestTournament");
		t.setPairingSystemPlugin(RoundRobinTest.PAIRING_PLUGIN);
		t.setPairingSystem(RoundRobinTest.ALGORITHM_CLASS);
		t.setEloCalculator("ch.jester.system.fidecalculator.FideCalculator");
		Category cat = new Category();
		t.addCategory(cat);
		cat.setDescription("HT1");
		cat.setPlayerCards(initPlayerCards(cat));
		initPairings(cat);
		initResults(cat);
		
		if(clz==Tournament.class){
			List<T> inputList = new ArrayList<T>();
			inputList.add((T) t);
			return inputList;
		}
		if(clz==Category.class){
			return (List<T>) t.getCategories();
		}
		if(clz==Player.class){
			return (List<T>) t.getCategories().get(0).getPlayers();
		}
		
		return null;
	}
	private List<PlayerCard> initPlayerCards(Category cat) {
		List<Player> players = new ArrayList<Player>();
		Player p1 = new Player();
		p1.setFirstName("a");
		p1.setLastName("A");
		Player p2 = new Player();
		p2.setFirstName("b");
		p2.setLastName("B");
		Player p3 = new Player();
		p3.setFirstName("c");
		p3.setLastName("C");
		Player p4 = new Player();
		p4.setFirstName("d");
		p4.setLastName("D");
		Player p5 = new Player();
		p5.setFirstName("e");
		p5.setLastName("E");
		Player p6 = new Player();
		p6.setFirstName("f");
		p6.setLastName("F");
		Player p7 = new Player();
		p7.setFirstName("g");
		p7.setLastName("G");
		players.add(p1);
		players.add(p2);
		players.add(p3);
		players.add(p4);
		players.add(p5);
		players.add(p6);
		players.add(p7);
		List<PlayerCard> playerCards = new ArrayList<PlayerCard>();
		for (Player player : players) {
			//PlayerCard playerCard = new PlayerCard();
			//playerCard.setPlayer(player);
			PlayerCard playerCard = ModelFactory.getInstance().createPlayerCard(cat, player);
			//playerCard.addRankingSystemPoint(new RankingSystemPoint(RANKINGSYSTEM_TYPE));
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
		PlayerCard p5 = playerCards.get(4);
		PlayerCard p6 = playerCards.get(5);
		PlayerCard p7 = playerCards.get(6);
		
		Round r1 = new Round();
		r1.setNumber(1);
		// Alle Pairings Spieler A 
		Pairing pair1 = new Pairing();
		pair1.setWhite(p1);
		pair1.setBlack(p2);
		Pairing pair2 = new Pairing();
		pair2.setWhite(p1);
		pair2.setBlack(p3);
		Pairing pair3 = new Pairing();
		pair3.setWhite(p1);
		pair3.setBlack(p4);
		Pairing pair4 = new Pairing();
		pair4.setWhite(p1);
		pair4.setBlack(p5);
		Pairing pair5 = new Pairing();
		pair5.setWhite(p1);
		pair5.setBlack(p6);
		Pairing pair6 = new Pairing();
		pair6.setWhite(p1);
		pair6.setBlack(p7);
		// Alle restlichen Pairings Spieler B
		Pairing pair7 = new Pairing();
		pair7.setWhite(p2);
		pair7.setBlack(p3);
		Pairing pair8 = new Pairing();
		pair8.setWhite(p2);
		pair8.setBlack(p4);
		Pairing pair9 = new Pairing();
		pair9.setWhite(p2);
		pair9.setBlack(p5);
		Pairing pair10 = new Pairing();
		pair10.setWhite(p2);
		pair10.setBlack(p6);
		Pairing pair11 = new Pairing();
		pair11.setWhite(p2);
		pair11.setBlack(p7);
		// Alle restlichen Pairings Spieler C
		Pairing pair12 = new Pairing();
		pair12.setWhite(p3);
		pair12.setBlack(p4);
		Pairing pair13 = new Pairing();
		pair13.setWhite(p3);
		pair13.setBlack(p5);
		Pairing pair14 = new Pairing();
		pair14.setWhite(p3);
		pair14.setBlack(p6);
		Pairing pair15 = new Pairing();
		pair15.setWhite(p3);
		pair15.setBlack(p7);
		// Alle restlichen Pairings Spieler D
		Pairing pair16 = new Pairing();
		pair16.setWhite(p4);
		pair16.setBlack(p5);
		Pairing pair17 = new Pairing();
		pair17.setWhite(p4);
		pair17.setBlack(p6);
		Pairing pair18 = new Pairing();
		pair18.setWhite(p4);
		pair18.setBlack(p7);
		// Alle restlichen Pairings Spieler E
		Pairing pair19 = new Pairing();
		pair19.setWhite(p5);
		pair19.setBlack(p6);
		Pairing pair20 = new Pairing();
		pair20.setWhite(p5);
		pair20.setBlack(p7);
		// Restliches Pairing
		Pairing pair21 = new Pairing();
		pair21.setWhite(p6);
		pair21.setBlack(p7);
		
		r1.addPairing(pair1);
		r1.addPairing(pair2);
		r1.addPairing(pair3);
		r1.addPairing(pair4);
		r1.addPairing(pair5);
		r1.addPairing(pair6);
		r1.addPairing(pair7);
		r1.addPairing(pair8);
		r1.addPairing(pair9);
		r1.addPairing(pair10);
		r1.addPairing(pair11);
		r1.addPairing(pair12);
		r1.addPairing(pair13);
		r1.addPairing(pair14);
		r1.addPairing(pair15);
		r1.addPairing(pair16);
		r1.addPairing(pair17);
		r1.addPairing(pair18);
		r1.addPairing(pair19);
		r1.addPairing(pair20);
		r1.addPairing(pair21);
		
		cat.addRound(r1);
//		cat.addRound(r2);
//		cat.addRound(r3);
	}
	
	private void initResults(Category cat) {
		List<Round> rounds = cat.getRounds();
		Round round = rounds.get(0);
		List<Pairing> pairings = round.getPairings();
		// Resultate Spieler A
		pairings.get(0).setResult("X");	// A:B
		pairings.get(0).getWhite().addResult(0.5);
		pairings.get(0).getBlack().addResult(0.5);
		pairings.get(1).setResult("X"); // A:C
		pairings.get(1).getWhite().addResult(0.5);
		pairings.get(1).getBlack().addResult(0.5);
		pairings.get(2).setResult("1"); // A:D
		pairings.get(2).getWhite().addResult(1.0);
		pairings.get(3).setResult("1"); // A:E
		pairings.get(3).getWhite().addResult(1.0);
		pairings.get(4).setResult("1"); // A:F
		pairings.get(4).getWhite().addResult(1.0);
		pairings.get(5).setResult("1"); // A:G
		pairings.get(5).getWhite().addResult(1.0);
		// Resultate Spieler B
		pairings.get(6).setResult("X");	// B:C
		pairings.get(6).getWhite().addResult(0.5);
		pairings.get(6).getBlack().addResult(0.5);
		pairings.get(7).setResult("X"); // B:D
		pairings.get(7).getWhite().addResult(0.5);
		pairings.get(7).getBlack().addResult(0.5);
		pairings.get(8).setResult("1"); // B:E
		pairings.get(8).getWhite().addResult(1.0);
		pairings.get(9).setResult("1"); // B:F
		pairings.get(9).getWhite().addResult(1.0);
		pairings.get(10).setResult("1"); // B:G
		pairings.get(10).getWhite().addResult(1.0);
		// Resultate Spieler C
		pairings.get(11).setResult("X"); // C:D
		pairings.get(11).getWhite().addResult(0.5);
		pairings.get(11).getBlack().addResult(0.5);
		pairings.get(12).setResult("X"); // C:E
		pairings.get(12).getWhite().addResult(0.5);
		pairings.get(12).getBlack().addResult(0.5);
		pairings.get(13).setResult("1"); // C:F
		pairings.get(13).getWhite().addResult(1.0);
		pairings.get(14).setResult("1"); // C:G
		pairings.get(14).getWhite().addResult(1.0);
		// Resultate Spieler D
		pairings.get(15).setResult("1"); // D:E
		pairings.get(15).getWhite().addResult(1.0);
		pairings.get(16).setResult("1"); // D:F
		pairings.get(16).getWhite().addResult(1.0);
		pairings.get(17).setResult("1"); // D:G
		pairings.get(17).getWhite().addResult(1.0);

	}
	
}
