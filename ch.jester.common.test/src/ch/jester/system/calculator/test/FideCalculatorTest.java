package ch.jester.system.calculator.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.Test;

import ch.jester.common.test.internal.ActivatorProviderForTestCase;
import ch.jester.commonservices.util.ServiceUtility;
import ch.jester.system.api.calculator.IEloCalculator;
import ch.jester.system.api.calculator.IEloCalculatorEntry;
import ch.jester.system.api.calculator.IEloCalculatorManager;

public class FideCalculatorTest extends ActivatorProviderForTestCase {

	private ServiceUtility mServiceUtil = new ServiceUtility(); 
	
	@Test
	public void testGetFideEloCalculatorService() {
		IEloCalculatorManager eloCalcManager = mServiceUtil.getService(IEloCalculatorManager.class);
		assertNotNull("EloCalculator is null", eloCalcManager);
		
		assertNotNull(getFideEloCalculatorService());
	}
	
	@Test
	public void testCalculateEloOneGame() {
		IEloCalculator fideCalculator = getFideEloCalculatorService();
		assertEquals(1714, fideCalculator.calculateElo(1720, 15, 1800, 0));
		assertEquals(1722, fideCalculator.calculateElo(1720, 15, 1800, 0.5));
		assertEquals(1729, fideCalculator.calculateElo(1720, 15, 1800, 1));
	}
	
	/**
	 * Sucht nach dem FideEloCalculator
	 * @return
	 */
	private IEloCalculator getFideEloCalculatorService() {
		IEloCalculatorManager eloCalcManager = mServiceUtil.getService(IEloCalculatorManager.class);
		
		List<IEloCalculatorEntry> registredEntries = eloCalcManager.getRegistredEntries();
		for (IEloCalculatorEntry iEloCalculatorEntry : registredEntries) {
			if (iEloCalculatorEntry.getShortType().contains("FIDE")) {
				return iEloCalculatorEntry.getService();
			}
		}
		return null;
	}
}
