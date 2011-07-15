package ch.jester.common.tests;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
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
import ch.jester.model.Player;
import ch.jester.model.PlayerCard;
import ch.jester.model.RankingSystem;
import ch.jester.model.RankingSystemPoint;
import ch.jester.model.Tournament;
import ch.jester.model.factories.ModelFactory;
import ch.jester.system.pairing.test.RoundRobinTest;
import ch.jester.system.ranking.test.BuchholzTest;

public class ImportExportTest {
	ModelFactory factory = ModelFactory.getInstance();
	ServiceUtility su = new ServiceUtility();
	ZipSerializationReader reader;
	SerializationWriter writer ;
	public Tournament createAndSaveTournament(){
		RankingSystem rankingSystem = new RankingSystem();
		rankingSystem.setPluginId(BuchholzTest.PLUGIN_ID);
		rankingSystem.setImplementationClass(BuchholzTest.RANKINGSYSTEM_CLASS);
		rankingSystem.setShortType(BuchholzTest.RANKINGSYSTEM_TYPE);
		rankingSystem.setRankingSystemNumber(1);
		
		Tournament tournament = factory.createTournament("TestTournament");
		tournament.setDateFrom(new Date());
		tournament.setDateTo(new Date());
		tournament.setPairingSystemPlugin(RoundRobinTest.PAIRING_PLUGIN);
		tournament.setPairingSystem("a");
		tournament.addRankingSystem(rankingSystem);
		tournament.setEloCalculator("d");
		
		
		Category cat1 = factory.createCategory("Cat1");
		Category cat2 = factory.createCategory("Cat2");
		
		Player p1 = factory.createPlayer();
		PlayerCard pc1 = factory.createPlayerCard(cat1, p1);
		p1.setLastName("p1");
		
		
		Player p2 = factory.createPlayer();
		PlayerCard pc2 = factory.createPlayerCard(cat1, p2);
		p2.setLastName("p2");
		
		tournament.addCategory(cat1);
		tournament.addCategory(cat2);
		
		cat1.addPlayerCard(pc1);
		cat1.addPlayerCard(pc2);
		
		RankingSystemPoint sp1 = new RankingSystemPoint("p1");
		RankingSystemPoint sp2 = new RankingSystemPoint("p2");
		
		pc1.addRankingSystemPoint(sp1);
		pc2.addRankingSystemPoint(sp2);
		
		su.getDaoServiceByEntity(Tournament.class).save(tournament);
		
		return tournament;
	}
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
		createAndSaveTournament();
		List<Tournament> tlist = su.getDaoServiceByEntity(Tournament.class).getAll();
		Assert.assertEquals(1, tlist.size());
		
		List<Category> clist = su.getDaoServiceByEntity(Category.class).getAll();
		Assert.assertEquals(2, clist.size());
		
		List<Player> plist = su.getDaoServiceByEntity(Player.class).getAll();
		Assert.assertEquals(2, plist.size());
		
		ObjectXMLSerializer serializer = new ObjectXMLSerializer();
		serializer.prepareContext(factory.getAllExportableClasses());
		
		writer =  serializer.createWriter("test1.zip");
		writer.newEntry("jester-export.xml");
		writer.write(tlist);
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
		Assert.assertEquals(2, tournament.getCategories().size());
		
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
}
