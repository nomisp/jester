package ch.jester.system.calculator.test;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import ch.jester.common.test.internal.ActivatorProviderForTestCase;
import ch.jester.commonservices.api.components.IComponentService;
import ch.jester.commonservices.util.ServiceUtility;
import ch.jester.system.api.calculator.IEloCalculatorEntry;
import ch.jester.system.api.calculator.IEloCalculatorManager;

/**
 * Testklasse für allgemeine Tests von Wertungszahl-Rechnern 
 *
 */
public class CalculatorTest extends ActivatorProviderForTestCase {

	private ServiceUtility mServiceUtil = new ServiceUtility(); 
	
	/**
	 * Test holen des Calculator-Service
	 * Test-ID: U-SC-1 
	 */
	@Test
	public void testGetEloCalculatorService() {
		IComponentService<?> eloCalculatorService = getActivationContext().getService(IEloCalculatorManager.class);
		assertNotNull("EloCalculator is null", eloCalculatorService);
		
		IEloCalculatorManager eloCalcManager = mServiceUtil.getService(IEloCalculatorManager.class);
		List<IEloCalculatorEntry> registredEntries = eloCalcManager.getRegistredEntries();
		
		assertEquals("Number of Elo-Calculators does not match", 1, registredEntries.size());
	}
}
