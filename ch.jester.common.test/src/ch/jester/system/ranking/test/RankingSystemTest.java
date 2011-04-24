package ch.jester.system.ranking.test;

import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.Test;

import ch.jester.common.test.internal.ActivatorProviderForTestCase;
import ch.jester.commonservices.api.components.IComponentService;
import ch.jester.commonservices.util.ServiceUtility;
import ch.jester.system.api.ranking.IRankingSystemEntry;
import ch.jester.system.api.ranking.IRankingSystemManager;

public class RankingSystemTest extends ActivatorProviderForTestCase {
	private ServiceUtility mServiceUtil = new ServiceUtility();
	
	@Test
	public void testGetRankingSystemService() {
		IComponentService<?> rankingSystemService = getActivationContext().getService(IRankingSystemManager.class);
		assertNotNull("RankingSystemManager is null", rankingSystemService);
		
		IRankingSystemManager rankingSystemManager = mServiceUtil.getService(IRankingSystemManager.class);
		List<IRankingSystemEntry> registredEntries = rankingSystemManager.getRegistredEntries();
		for (IRankingSystemEntry rankingSystemEntry : registredEntries) {
			System.out.println(rankingSystemEntry.getShortType());
		}
	}
}
