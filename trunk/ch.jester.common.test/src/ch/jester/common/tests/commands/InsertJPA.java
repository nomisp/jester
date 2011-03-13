package ch.jester.common.tests.commands;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

import ch.jester.common.tests.DummyImportDBPerf;



public class InsertJPA extends AbstractHandler{

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		DummyImportDBPerf.testImportJPA();
		return null;
	}
	
}