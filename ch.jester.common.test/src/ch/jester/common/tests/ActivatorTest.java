package ch.jester.common.tests;

import junit.framework.Assert;

import org.junit.Test;

import ch.jester.common.test.internal.ActivatorProviderForTestCase;

public class ActivatorTest extends ActivatorProviderForTestCase {

	/**
	 * Ist der Activator vorhanden?
	 */
	@Test
	public void testGetActivator() {
		Assert.assertNotNull("TestActivator is null", getActivator());
	}

	/**
	 * gibt es einen gültigen BundleContext?
	 */
	@Test
	public void testGetBundleContext() {
		Assert.assertNotNull("BundleContext is null", getActivationContext()
				.getBundleContext());
	}

	/**
	 * stimmt die PlugInId?
	 */
	@Test
	public void testGetPluginId() {
		Assert.assertEquals("PlugIn Id is diff: ", getActivationContext()
				.getPluginId(), "ch.jester.common.test");
	}
}
