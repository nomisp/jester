package ch.jester.common.tests.testservice;

import ch.jester.common.importer.AbstractImportHandler;



public class DummyImportHandler extends AbstractImportHandler{

	public DummyImportHandler(){
		mLogger.info("DummyImportHandler started");
	}
	
	
	@Override
	public Object handleImport(Object o) {
		mLogger.info("uhhh... doing a dummy import... return Object will be 55");
		return "55";
	}

}
