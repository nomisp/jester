package ch.jester.common.tests;

import junit.framework.Assert;

import org.junit.Test;

import ch.jester.common.test.internal.ActivatorProviderForTestCase;
import ch.jester.commonservices.api.components.IComponentService;
import ch.jester.commonservices.api.importer.IImportManager;

public class ImporterTest extends ActivatorProviderForTestCase {

	/**
	 * Ist ein IImportManagerService vorhanden
	 * Test-ID: U-IE-2
	 */
	@Test
	public void testGetImporterService() {
		System.out.println("testGetImporterService");
		IComponentService<?> importManager = getActivationContext().getService(IImportManager.class);
		Assert.assertNotNull("Import Manager is null", importManager);
	}
	/**
	 * Testet einen Eintrag im ImportManager
	 * Test-ID: U-IE-3
	 */
	@Test
	public void testGetImporterHandler() {

		System.out.println("testGetImporterHandler");
		
		IImportManager importManager = getActivationContext().getService(IImportManager.class);
		Assert.assertNotNull("Import Manager is null", importManager);
		
		//List<IImportHandlerEntry> list = importManager.getRegistredEntries();
		//IImportHandlerEntry entry = null;
	/*	for(IImportHandlerEntry e:list){
			if(e.getDescription().startsWith("FIDE")){
				entry=e;
				break;
			}
		}
		
		try {
			entry.getService().handleImport(new FileInputStream("C:/Users/matthias/Desktop/players_list.txt"), new NullProgressMonitor());
		} catch (FileNotFoundException e) {

			e.printStackTrace();
		}
		
		IPlayerDao persister = getActivationContext().getService(IPlayerDao.class);
		int reps = persister.count();
		for(int i = 0; i<reps;i=i+50){
			persister.getFromTo(i, i+50);
		}*/
	}

}