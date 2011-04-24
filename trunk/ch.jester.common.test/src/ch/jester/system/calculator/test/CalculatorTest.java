package ch.jester.system.calculator.test;

import static org.junit.Assert.*;

import org.junit.Test;

import ch.jester.common.test.internal.ActivatorProviderForTestCase;
import ch.jester.commonservices.api.components.IComponentService;
import ch.jester.system.api.calculator.IEloCalculatorManager;

public class CalculatorTest extends ActivatorProviderForTestCase {

	@Test
	public void testGetEloCalculatorService() {
		IComponentService<?> eloCalculatorService = getActivationContext().getService(IEloCalculatorManager.class);
		assertNotNull("EloCalculator is null", eloCalculatorService);
	}
}
