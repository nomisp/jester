package ch.jester.common.tests.testservice;

import java.io.InputStream;

import org.eclipse.core.runtime.IProgressMonitor;


import ch.jester.common.test.internal.TestActivator;
import ch.jester.commonservices.api.importer.IImportHandler;
import ch.jester.commonservices.api.logging.ILogger;



public class DummyImportHandler implements IImportHandler{
	ILogger mLogger = TestActivator.getActivator().getActivationContext().getLogger();
	public DummyImportHandler(){
		mLogger.info("DummyImportHandler started");
	}
	
	@Override
	public Object handleImport(InputStream o, IProgressMonitor pMonitor) {
		mLogger.info("uhhh... doing a dummy import... return Object will be 55");
		return "55";
	}

	@Override
	public Object getAdapter(Class adapter) {
		// TODO Auto-generated method stub
		return null;
	}


}
