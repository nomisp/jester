package ch.jester.db.hsqldb.tools;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.hsqldb.util.DatabaseManagerSwing;

public class StartTool extends AbstractHandler{

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		new DatabaseManagerSwing().main();
		return null;
	}

	
}
