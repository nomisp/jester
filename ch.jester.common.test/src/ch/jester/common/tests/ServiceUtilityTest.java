package ch.jester.common.tests;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import ch.jester.common.test.internal.ActivatorProviderForTestCase;
import ch.jester.common.tests.testservice.DefaultDummyService;
import ch.jester.common.tests.testservice.api.IDummyService;

public class ServiceUtilityTest extends ActivatorProviderForTestCase {
	private static IDummyService mDummyService;
	private static boolean serviceRegistred;

	@Before
	public void setUp() throws Exception {
		addDummyService();
	}

	/**
	 * Registrierung eines einzigen DummyServices für den ganzen Testrun.<br>
	 */
	private void addDummyService() {
		if (!serviceRegistred) {
			serviceRegistred = true;
			mDummyService = new DefaultDummyService();
			getActivationContext().getServiceUtil().registerService(
					IDummyService.class, mDummyService);
		}

	}

	/**
	 * Simples getService() des im setup() bereitgestellten Services
	 * Test-ID: U-CO-5
	 */
	@Test
	public void testGetService() {
		IDummyService dummyServiceFromUtil = getActivationContext().getServiceUtil()
				.getService(IDummyService.class);
		Assert.assertNotNull("DummyService is null", dummyServiceFromUtil);
		Assert.assertEquals("DummyServicObjectRef is not equal",
				dummyServiceFromUtil, mDummyService);
	}

	/**
	 * MethodenCall auf den DummyService
	 * Test-ID: U-CO-6
	 */
	@Test
	public void testGet() {
		IDummyService dummyService = getActivator().getActivationContext()
				.getService(IDummyService.class);
		Assert.assertEquals("Result is not 55", 55, dummyService.get());

	}

}
