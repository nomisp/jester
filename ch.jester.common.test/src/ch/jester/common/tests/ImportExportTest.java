package ch.jester.common.tests;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import ch.jester.common.utility.ObjectXMLSerializer;
import ch.jester.common.utility.ObjectXMLSerializer.SerializationWriter;
import ch.jester.common.utility.ObjectXMLSerializer.ZipSerializationReader;
import ch.jester.commonservices.api.persistency.IEntityObject;
import ch.jester.commonservices.util.ServiceUtility;
import ch.jester.model.Category;
import ch.jester.model.Pairing;
import ch.jester.model.Player;
import ch.jester.model.PlayerCard;
import ch.jester.model.RankingSystem;
import ch.jester.model.RankingSystemPoint;
import ch.jester.model.Round;
import ch.jester.model.Tournament;
import ch.jester.model.factories.ModelFactory;
import ch.jester.orm.ORMPlugin;
import ch.jester.system.api.ranking.IRankingSystem;
import ch.jester.system.api.ranking.IRankingSystemEntry;
import ch.jester.system.api.ranking.IRankingSystemManager;
import ch.jester.system.pairing.test.RoundRobinTest;
import ch.jester.system.ranking.test.BuchholzTest;

public class ImportExportTest {
	ModelFactory factory = ModelFactory.getInstance();
	ServiceUtility su = new ServiceUtility();
	ZipSerializationReader reader;
	SerializationWriter writer ;
	
	
	//Turnierdaten
	public static final String PLUGIN_ID = "ch.jester.rankingsystem.sonnebornberger";
	public static final String RANKINGSYSTEM_CLASS = "ch.jester.rankingsystem.sonnebornberger.SonnebornBergerRankingSystem";
	public static final String RANKINGSYSTEM_TYPE = "Sonneborn-Berger";
	private IRankingSystem sonnebornbergerSystem;
	private RankingSystem rankingSystem;
	private Tournament t;
	private Category cat;

	@After
	public void cleanup() throws IOException{
		if(writer!=null){
			writer.close();
		}
		new File("test1.zip").delete();
		if(reader!=null){
			reader.close();
		}
	}
	
	public void testExport() throws JAXBException, IOException{
		setupComplexTournament();
		ObjectXMLSerializer serializer = new ObjectXMLSerializer();
		serializer.prepareContext(ModelFactory.getInstance().getAllExportableClasses());
		writer =  serializer.createWriter("test1.zip");
		writer.newEntry("jester-export.xml");
		List<Tournament> list = new ArrayList<Tournament>();
		list.add(t);
		writer.write(list);
		writer.close();
		writer=null;

	}
	
	@SuppressWarnings("unchecked")
	public void doImport() throws JAXBException, FileNotFoundException, IOException{
		ObjectXMLSerializer serializer = new ObjectXMLSerializer();
		serializer.prepareContext(factory.getAllExportableClasses());
		
		 reader = serializer.createReader("test1.zip");
		List<IEntityObject> list = reader.read("jester-export.xml");
		
		Assert.assertEquals(1, list.size());
		Tournament loadedTournament =  (Tournament) list.get(0);
		Assert.assertEquals(Tournament.class, list.get(0).getClass());
		Tournament tournament = (Tournament) list.get(0);
		Assert.assertEquals(1, tournament.getCategories().size());
		
		Assert.assertEquals(1, tournament.getRankingSystems().size());
		Assert.assertEquals(loadedTournament, loadedTournament.getRankingSystems().get(0).getTournament());
		
		Category cat = null;
		Iterator<Category> catit = tournament.getCategories().iterator();
		while(catit.hasNext()){
			Category c = catit.next();
			if(c.getDescription().equals("Cat1")){
				cat  = c;
				break;
			}
		}
		Assert.assertNotNull(cat);
		Assert.assertEquals(2, cat.getPlayerCards().size());
		su.getDaoServiceByEntity(Tournament.class).save(tournament);
	}
	
	@Test
	public void testExportImport() throws JAXBException, IOException{
		testExport();
		doImport();
		//cleanup();
	}
	
	
	private Tournament setupComplexTournament(){
		ServiceUtility mServiceUtil = new ServiceUtility();
		IRankingSystemManager rankingSystemManager = mServiceUtil.getService(IRankingSystemManager.class);
		List<IRankingSystemEntry> registredEntries = rankingSystemManager.getRegistredEntries();
		for (IRankingSystemEntry rankingSystemEntry : registredEntries) {
			if (rankingSystemEntry.getImplementationClass().equals(RANKINGSYSTEM_CLASS)) {
				sonnebornbergerSystem = rankingSystemEntry.getService();
				break;
			}
		}

		rankingSystem = new RankingSystem();
		rankingSystem.setPluginId(PLUGIN_ID);
		rankingSystem.setImplementationClass(RANKINGSYSTEM_CLASS);
		rankingSystem.setShortType(RANKINGSYSTEM_TYPE);
		rankingSystem.setRankingSystemNumber(1);
		
		t = new Tournament();
		t.setName("SonnebornBergerTestTournament");
		t.addRankingSystem(rankingSystem);
		t.setPairingSystemPlugin(RoundRobinTest.PAIRING_PLUGIN);
		t.setPairingSystem(RoundRobinTest.ALGORITHM_CLASS);
		t.setEloCalculator("ch.jester.system.fidecalculator.FideCalculator");
		cat = new Category();
		cat.setDescription("HT1");
		cat.setPlayerCards(initPlayerCards());
		initPairings(cat);
		initResults();
		t.addCategory(cat);
		//mServiceUtil.getDaoServiceByEntity(Tournament.class).save(t);
		return t;
	}
	private List<PlayerCard> initPlayerCards() {
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
	
	private void initResults() {
		List<Round> rounds = cat.getRounds();
		Round round = rounds.get(0);
		List<Pairing> pairings = round.getPairings();
		// Resultate Spieler A
		pairings.get(0).setResult("X");	// A:B
		pairings.get(1).setResult("X"); // A:C
		pairings.get(2).setResult("1"); // A:D
		pairings.get(3).setResult("1"); // A:E
		pairings.get(4).setResult("1"); // A:F
		pairings.get(5).setResult("1"); // A:G
		// Resultate Spieler B
		pairings.get(6).setResult("X");	// B:C
		pairings.get(7).setResult("X"); // B:D
		pairings.get(8).setResult("1"); // B:E
		pairings.get(9).setResult("1"); // B:F
		pairings.get(10).setResult("1"); // B:G
		// Resultate Spieler C
		pairings.get(11).setResult("X"); // C:D
		pairings.get(12).setResult("X"); // C:E
		pairings.get(13).setResult("1"); // C:F
		pairings.get(14).setResult("1"); // C:G
		// Resultate Spieler D
		pairings.get(15).setResult("1"); // D:E
		pairings.get(16).setResult("1"); // D:F
		pairings.get(17).setResult("1"); // D:G
		// Resultate Spieler E
		pairings.get(18).setResult("1"); // E:F
		pairings.get(19).setResult("1"); // E:G
		// Resultat Spieler F
		pairings.get(20).setResult("1"); // F:G
		
		for(Pairing par:pairings){
			System.out.println(par + "---"+ par.getWhite().getRankingSystemPoints().get(0).getPoints()+" --- "+par.getBlack().getRankingSystemPoints().get(0).getPoints());
		}
		
	}
}
