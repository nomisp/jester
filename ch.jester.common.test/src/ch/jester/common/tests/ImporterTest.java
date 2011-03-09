package ch.jester.common.tests;

import java.util.List;

import junit.framework.Assert;

import org.junit.Test;

import ch.jester.common.test.internal.ActivatorProviderForTestCase;
import ch.jester.commonservices.api.importer.IImportHandler;
import ch.jester.commonservices.api.importer.IImportHandlerEntry;
import ch.jester.commonservices.api.importer.IImportManager;

public class ImporterTest extends ActivatorProviderForTestCase {

	/**
	 * Ist der Activator vorhanden?
	 */
	@Test
	public void testGetImporterService() {
		System.out.println("testGetImporterService");
		IImportManager importManager = getActivationContext().getService(IImportManager.class);
		Assert.assertNotNull("Import Manager is null", importManager);
	}
	@Test
	public void testGetImporterHandler() {

		System.out.println("testGetImporterHandler");
		
		IImportManager importManager = getActivationContext().getService(IImportManager.class);
		Assert.assertNotNull("Import Manager is null", importManager);
		
		List<IImportHandlerEntry<IImportHandler>> list = importManager.getRegistredImportHandlers();
		Assert.assertEquals("Import Handlers size diff", 1, list.size());
		IImportHandlerEntry<IImportHandler> entry = list.get(0);
		
		IImportHandler handler = list.get(0).getService();
		handler.handleImport("blabla");
	}

}