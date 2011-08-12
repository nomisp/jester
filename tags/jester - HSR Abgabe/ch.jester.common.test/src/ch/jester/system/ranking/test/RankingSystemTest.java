package ch.jester.system.ranking.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.Test;

import ch.jester.common.test.internal.ActivatorProviderForTestCase;
import ch.jester.commonservices.api.components.IComponentService;
import ch.jester.commonservices.util.ServiceUtility;
import ch.jester.system.api.ranking.IRankingSystemEntry;
import ch.jester.system.api.ranking.IRankingSystemManager;

/**
 * Allgemeine Tests für die Fgeinwertungssysteme (Services)
 *
 */
public class RankingSystemTest extends ActivatorProviderForTestCase {
	private ServiceUtility mServiceUtil = new ServiceUtility();
	
	/**
	 * Test holen des Feinwertungssystem-Service
	 * Test-ID: U-SR-1
	 */
	@Test
	public void testGetRankingSystemService() {
		IComponentService<?> rankingSystemService = getActivationContext().getService(IRankingSystemManager.class);
		assertNotNull("RankingSystemManager is null", rankingSystemService);
		
		IRankingSystemManager rankingSystemManager = mServiceUtil.getService(IRankingSystemManager.class);
		List<IRankingSystemEntry> registredEntries = rankingSystemManager.getRegistredEntries();

		IRankingSystemEntry rankingSystemEntry = registredEntries.get(0);
		if (rankingSystemEntry.getShortType().startsWith("So")) { // da die Reihenfolge der Services nicht definiert ist
			assertEquals("Sonneborn-Berger", rankingSystemEntry.getShortType());
		} else {
			assertEquals("Buchholz", rankingSystemEntry.getShortType());
		}
		
		rankingSystemEntry = registredEntries.get(1);
		if (rankingSystemEntry.getShortType().startsWith("So")) {
			assertEquals("Sonneborn-Berger", rankingSystemEntry.getShortType());
		} else {
			assertEquals("Buchholz", rankingSystemEntry.getShortType());
		}
	}
}

