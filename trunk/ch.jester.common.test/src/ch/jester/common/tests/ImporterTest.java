package ch.jester.common.tests;

import java.util.List;

import junit.framework.Assert;

import org.junit.Test;

import ch.jester.common.test.internal.ActivatorProviderForTestCase;
import ch.jester.commonservices.api.importer.IComponentService;
import ch.jester.commonservices.api.importer.IImportHandlerEntry;
import ch.jester.commonservices.api.importer.IImportManager;

public class ImporterTest extends ActivatorProviderForTestCase {

	/**
	 * Ist ein IImportManagerService vorhanden
	 */
	@Test
	public void testGetImporterService() {
		System.out.println("testGetImporterService");
		IComponentService<?> importManager = getActivationContext().getService(IImportManager.class);
		Assert.assertNotNull("Import Manager is null", importManager);
	}
	/**
	 * Testet einen Eintrag im ImportManager
	 */
	@Test
	public void testGetImporterHandler() {

		System.out.println("testGetImporterHandler");
		
		IImportManager importManager = getActivationContext().getService(IImportManager.class);
		Assert.assertNotNull("Import Manager is null", importManager);
		
		List<IImportHandlerEntry> list = importManager.getRegistredImportHandlers();
		Assert.assertEquals("Import Handlers size diff", 1, list.size());
		IImportHandlerEntry entry = list.get(0);
		
		Assert.assertEquals("shorttype does not match","*.xml", entry.getShortType());
		Assert.assertEquals("description does not match","XML Importer", entry.getDescription());
		importManager.doImport(entry, null);
	}

}