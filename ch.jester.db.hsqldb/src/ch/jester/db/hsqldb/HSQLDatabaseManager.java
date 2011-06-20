package ch.jester.db.hsqldb;

import java.sql.SQLException;

import org.hsqldb.Server;

import ch.jester.orm.IDatabaseManager;
import ch.jester.orm.ORMPlugin;


public class HSQLDatabaseManager implements IDatabaseManager {
	private Server server;
	private String mDbOptions = ";hsqldb.default_table_type=cached;hsqldb.tx=mvcc";
	public HSQLDatabaseManager() {
		server = new Server();
	}

	@Override
	public void start() {
		String ip = ORMPlugin.getConfiguration().getDefaultPath();
		
		server.setDatabaseName(0, ORMPlugin.getConfiguration().getDbname());
		server.setDatabasePath(0, ip+"/"+ORMPlugin.getConfiguration().getDbname()+mDbOptions);  
		//http://hsqldb.org/doc/2.0/guide/sessions-chapt.html#sqlgeneral_trans_cc-sect for mvcc
	
		server.start();

		
	}

	@Override
	public void stop() {
		
		try {
			
			// TODO geht das auch schöner?
			//beendet db richtig... alles andere funktioniert nicht so wirklich
			ORMPlugin.getConfiguration().getConnection().createStatement().execute("shutdown");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		//async call
		server.stop();
		
		//busy wait bis server fertig
		while((server.getState())!=16){};
	}


	@Override
	public void shutdown() {
		server.shutdown();
	}

	@Override
	public String getIP() {
		return "jdbc:hsqldb:hsql://localhost/"+ORMPlugin.getConfiguration().getDbname();
	}


}
