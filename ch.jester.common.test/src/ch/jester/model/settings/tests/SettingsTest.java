package ch.jester.model.settings.tests;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import ch.jester.common.settings.SettingHelper;
import ch.jester.common.test.internal.ActivatorProviderForTestCase;
import ch.jester.commonservices.api.persistency.IDaoService;
import ch.jester.commonservices.util.ServiceUtility;
import ch.jester.model.SettingItem;
import ch.jester.model.Tournament;
import ch.jester.model.factories.ModelFactory;
import ch.jester.orm.ORMPlugin;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;

public class SettingsTest extends ActivatorProviderForTestCase {

	private ServiceUtility mServiceUtil = new ServiceUtility();
	private IDaoService<SettingItem> settingItemPersister;
	private ModelFactory modelFactory;
	private Tournament tournament;
	
//	@BeforeClass
	public void setUp() {
		settingItemPersister = mServiceUtil.getDaoServiceByEntity(SettingItem.class);
		modelFactory = ModelFactory.getInstance();
		tournament = modelFactory.createTournament("SettingsTestTournament");
		tournament.setDescription("Testturnier für SettingsTest");
		tournament.setYear(2011);
		tournament.setPairingSystem("ch.jester.system.vollrundig.VollrundigPairingAlgorithm");
		tournament.setEloCalculator("ch.jester.system.fidecalculator.FideCalculator");
		tournament.setRankingSystem("ch.jester.rankingsystem.buchholz.BuchholzRankingSystem");
		mServiceUtil.getDaoServiceByEntity(Tournament.class).save(tournament);
	}
	
	@Test
	public void testPersistDummySettingObject() {
		setUp();
		DummySettingObject settings = new DummySettingObject();
		settings.setDoubleRounded(Boolean.FALSE);
		settings.setNow(new Date());
		settings.setPairingAlgorithm("ch.jester.system.TestPairingAlgorithm");
		List<String> strings = new ArrayList<String>();
		strings.add("String 1");
		strings.add("String 2");
		strings.add("String 3");
		settings.setStrings(strings);
		SettingHelper<DummySettingObject> settingHelper = new SettingHelper<DummySettingObject>();
		SettingItem settingItem = modelFactory.createSettingItem(tournament);
		settingItem = settingHelper.analyzeSettingObjectToStore(settings, settingItem);
		settingItemPersister.save(settingItem);
		
		Query query = settingItemPersister.createQuery("select s from SettingItem s");
		SettingItem retrievedSettingItem = (SettingItem)query.getSingleResult();
	}
	
	@Test
	public void testRetrieveSettingItemFromDB () {
		SettingItem retrievedSettingItem = retrieveSettingItem();
		
		assertEquals("ch.jester.model.settings.tests.DummySettingObject", retrievedSettingItem.getRootClassName());
		assertEquals(tournament, retrievedSettingItem.getTournament());
	}
	
	@Test
	public void testRestoreSettingItem() {
		SettingItem retrievedSettingItem = retrieveSettingItem();
		SettingHelper<DummySettingObject> settingHelper = new SettingHelper<DummySettingObject>();
		DummySettingObject settingObject = settingHelper.restoreSettingObject(new DummySettingObject(), retrievedSettingItem);
		
		assertEquals(Boolean.FALSE, settingObject.getDoubleRounded());
		assertEquals("ch.jester.system.TestPairingAlgorithm", settingObject.getPairingAlgorithm());
		List<String> strings = settingObject.getStrings();
		assertEquals("String 1", strings.get(0));
		assertEquals("String 2", strings.get(1));
		assertEquals("String 3", strings.get(2));
	}

	/**
	 * Laden des SettingItems aus der DB
	 * @return
	 */
	private SettingItem retrieveSettingItem() {
		settingItemPersister = mServiceUtil.getDaoServiceByEntity(SettingItem.class);
		EntityManagerFactory emf = ORMPlugin.getJPAEntitManagerFactor();
		EntityManager entityManager = emf.createEntityManager();
		tournament = (Tournament)entityManager.createQuery("select t from Tournament t where t.name = :tName")
								.setParameter("tName", "SettingsTestTournament").getSingleResult();
		Query query = entityManager.createQuery("select s from SettingItem s");
		SettingItem retrievedSettingItem = (SettingItem)query.getSingleResult();
		return retrievedSettingItem;
	}
}