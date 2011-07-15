package ch.jester.model.settings.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.junit.Before;
import org.junit.Test;

import ch.jester.common.settings.SettingHelper;
import ch.jester.common.test.internal.ActivatorProviderForTestCase;
import ch.jester.model.Category;
import ch.jester.model.RankingSystem;
import ch.jester.model.SettingItem;
import ch.jester.model.Tournament;
import ch.jester.model.factories.ModelFactory;
import ch.jester.orm.ORMPlugin;
import ch.jester.system.pairing.test.RoundRobinTest;
import ch.jester.system.ranking.test.BuchholzTest;

public class SettingsTest extends ActivatorProviderForTestCase {

	private ModelFactory modelFactory;
	private Tournament tournament;
	private Category cat;
	private EntityManager entityManager;
	
	@Before
	public void setUp() {
		entityManager = ORMPlugin.getJPAEntityManager();
		if (entityManager.getTransaction().isActive()) {
			entityManager.joinTransaction();
		} else {
			entityManager.getTransaction().begin();
		}
		entityManager.clear();
		
		RankingSystem rankingSystem = new RankingSystem();
		rankingSystem.setPluginId(BuchholzTest.PLUGIN_ID);
		rankingSystem.setImplementationClass(BuchholzTest.RANKINGSYSTEM_CLASS);
		rankingSystem.setShortType(BuchholzTest.RANKINGSYSTEM_TYPE);
		rankingSystem.setRankingSystemNumber(1);
		
		modelFactory = ModelFactory.getInstance();
		tournament = modelFactory.createTournament("SettingsTestTournament");
		tournament.setDescription("Testturnier für SettingsTest");
		tournament.setYear(2011);
		tournament.setPairingSystemPlugin(RoundRobinTest.PAIRING_PLUGIN);
		tournament.setPairingSystem(RoundRobinTest.ALGORITHM_CLASS);
		tournament.setEloCalculator("ch.jester.system.fidecalculator.FideCalculator");
		tournament.addRankingSystem(rankingSystem);
		cat = modelFactory.createCategory("HT1");
		tournament.addCategory(cat);
		
		entityManager.persist(tournament);
		entityManager.flush();
	}
	
//	@After
//	public void tearDown() {
//		entityManager.joinTransaction();
//		entityManager.remove(tournament);
//		entityManager.flush();
//	}
	
	@Test
	public void testPersistDummySettingObject() {
		DummySettingObject settings = new DummySettingObject();
		settings.setDoubleRounded(Boolean.FALSE);
		settings.setNow(new Date());
		settings.setPairingAlgorithm("ch.jester.system.TestPairingAlgorithm");
		settings.setNumberOfRounds(7);
		settings.setResult(3.5);
		SettingHelper<DummySettingObject> settingHelper = new SettingHelper<DummySettingObject>();
		SettingItem settingItem = modelFactory.createSettingItem(tournament);
		settingItem = settingHelper.analyzeSettingObjectToStore(settings, settingItem);
		entityManager.joinTransaction();
		entityManager.persist(settingItem);
		entityManager.flush();

		Query query = entityManager.createQuery("select s from SettingItem s where s.rootClassName ='ch.jester.model.settings.tests.DummySettingObject'");
		SettingItem retrievedSettingItem = (SettingItem)query.getSingleResult();
		assertNotNull(retrievedSettingItem);
//		List<SettingItem> resultList = query.getResultList();
//		assertNotNull(resultList.get(0));
	}
	
	@Test
	public void testRetrieveSettingItemFromDB () {
		SettingItem retrievedSettingItem = retrieveSettingItem();
		
		assertEquals("ch.jester.model.settings.tests.DummySettingObject", retrievedSettingItem.getRootClassName());
	}
	
	@Test
	public void testRestoreSettingItem() {
		SettingItem retrievedSettingItem = retrieveSettingItem();
		SettingHelper<DummySettingObject> settingHelper = new SettingHelper<DummySettingObject>();
		DummySettingObject settingObject = settingHelper.restoreSettingObject(new DummySettingObject(), retrievedSettingItem);
		
		assertEquals(Boolean.FALSE, settingObject.getDoubleRounded());
		assertEquals("ch.jester.system.TestPairingAlgorithm", settingObject.getPairingAlgorithm());
		assertEquals(new Integer(7), settingObject.getNumberOfRounds());
		assertEquals(new Double(3.5), settingObject.getResult());
	}

	/**
	 * Laden des SettingItems aus der DB
	 * @return
	 */
	private SettingItem retrieveSettingItem() {
		entityManager.clear();
		entityManager.joinTransaction();

		Query query = entityManager.createQuery("select s from SettingItem s where s.rootClassName ='ch.jester.model.settings.tests.DummySettingObject'");
		SettingItem retrievedSettingItem = (SettingItem)query.getSingleResult();
		
		return retrievedSettingItem;
	}
}
