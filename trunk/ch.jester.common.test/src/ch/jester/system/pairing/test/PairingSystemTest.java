package ch.jester.system.pairing.test;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import ch.jester.common.test.internal.ActivatorProviderForTestCase;
import ch.jester.commonservices.api.components.IComponentService;
import ch.jester.commonservices.util.ServiceUtility;
import ch.jester.system.api.pairing.IPairingAlgorithmEntry;
import ch.jester.system.api.pairing.IPairingManager;

/**
 * Allgemeine Tests für die Paarungssystem (Services)
 *
 */
public class PairingSystemTest extends ActivatorProviderForTestCase {
	private ServiceUtility mServiceUtil = new ServiceUtility(); 
	
	/**
	 * Test holen des Paarungs-Service
	 * Test-ID: U-SP-1
	 */
	@Test
	public void testGetPairingAlgorithmService() {
		IComponentService<?> pairingService = getActivationContext().getService(IPairingManager.class);
		assertNotNull("PairingManager is null", pairingService);
		
	}
	
	/**
	 * Test holen des Paarungsalgorithmus
	 * Test-ID: U-SP-2
	 */
	@Test
	public void testGetPairingAlgorithm() {
		IPairingManager pairingManager = mServiceUtil.getService(IPairingManager.class);
		List<IPairingAlgorithmEntry> registredEntries = pairingManager.getRegistredEntries();
		
		assertEquals("Number of pairing algorithms does not match", 2, registredEntries.size());
	}
}
