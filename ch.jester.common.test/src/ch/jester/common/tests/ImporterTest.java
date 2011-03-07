package ch.jester.common.tests;

import java.util.Collection;

import junit.framework.Assert;

import org.junit.Test;

import ch.jester.common.test.internal.ActivatorProviderForTestCase;
import ch.jester.common.tests.testservice.DummyImportHandler;
import ch.jester.commonservices.api.importer.IImportHandler;
import ch.jester.commonservices.api.importer.IImportHandlerEntry;
import ch.jester.commonservices.api.importer.IImportManager;

public class ImporterTest extends ActivatorProviderForTestCase {

	/**
	 * Ist der Activator vorhanden?
	 */
	@Test
	public void testGetImporterService() {
		IImportManager importManager = getActivationContext().getService(IImportManager.class);
		Assert.assertNotNull("Import Manager is null", importManager);
	}
	@Test
	public void testGetImporterHandler() {

		
		
		IImportManager importManager = getActivationContext().getService(IImportManager.class);
		Assert.assertNotNull("Import Manager is null", importManager);
		
		Collection<IImportHandlerEntry<?>> col = importManager.getRegistredImportHandlers();
		Assert.assertEquals("Import Handlers size diff", 1, col.size());
	}

}