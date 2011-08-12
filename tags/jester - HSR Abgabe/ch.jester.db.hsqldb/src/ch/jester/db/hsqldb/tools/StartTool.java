package ch.jester.db.hsqldb.tools;

import java.sql.Connection;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.hsqldb.util.DatabaseManagerSwing;

import ch.jester.orm.ORMPlugin;



/**
 * Handler um den HSQLDB Explorer zu starten
 *
 */
public class StartTool extends AbstractHandler{

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		Connection c = ORMPlugin.getConnection();
		DatabaseManagerSwing dms = new DatabaseManagerSwing();
		dms.main();
		dms.connect(c);
		return null;
	}

	
}
