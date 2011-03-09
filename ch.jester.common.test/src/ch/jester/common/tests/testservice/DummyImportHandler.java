package ch.jester.common.tests.testservice;

import java.io.InputStream;

import ch.jester.common.importer.AbstractImportHandler;



public class DummyImportHandler extends AbstractImportHandler{

	public DummyImportHandler(){
		mLogger.info("DummyImportHandler started");
	}
	
	
	@Override
	public Object handleImport(InputStream pInputStream) {
		mLogger.info("uhhh... doing a dummy import... return Object will be 55");
		return "55";
	}


	@Override
	public String getProperty(String pPropertyKey) {
		// TODO Auto-generated method stub
		return null;
	}



}