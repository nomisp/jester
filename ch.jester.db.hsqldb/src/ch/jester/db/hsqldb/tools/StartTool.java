package ch.jester.db.hsqldb.tools;

import java.sql.Connection;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.hsqldb.util.DatabaseManagerSwing;

import ch.jester.hibernate.helper.HibernatehelperPlugin;

public class StartTool extends AbstractHandler{

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		@SuppressWarnings("deprecation")
		Connection c = HibernatehelperPlugin.getSession().connection();
		DatabaseManagerSwing dms = new DatabaseManagerSwing();
		dms.main();
		dms.connect(c);
		return null;
	}

	
}
