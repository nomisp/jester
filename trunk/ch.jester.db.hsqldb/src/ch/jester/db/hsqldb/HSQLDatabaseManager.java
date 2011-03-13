package ch.jester.db.hsqldb;

import java.sql.SQLException;

import org.hibernate.HibernateException;
import org.hsqldb.Server;

import ch.jester.hibernate.helper.ConfigurationHelper;
import ch.jester.hibernate.helper.HibernatehelperPlugin;
import ch.jester.hibernate.helper.IDatabaseManager;

public class HSQLDatabaseManager implements IDatabaseManager {
	private Server server;
	private String mDbOptions = ";hsqldb.default_table_type=cached;hsqldb.tx=mvcc";
	public HSQLDatabaseManager() {
		server = new Server();
	}

	@Override
	public void start() {
		String ip = ConfigurationHelper.getDefaultPath();
		
		server.setDatabaseName(0, ConfigurationHelper.getDbname());
		server.setDatabasePath(0, ip+"/"+ConfigurationHelper.getDbname()+mDbOptions);  //;hsqldb.cache_scale=15 default_table_type=cached --> damit tables auch auf der platte landen
		//http://hsqldb.org/doc/2.0/guide/sessions-chapt.html#sqlgeneral_trans_cc-sect for mvcc
	
		server.start();

		
	}

	@Override
	public void stop() {
		
		try {
			
			// TODO geht das auch schöner?
			//beendet db richtig... alles andere funktioniert nicht so wirklich
			HibernatehelperPlugin.getSession().connection().createStatement().execute("shutdown");
		} catch (HibernateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
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
		return "jdbc:hsqldb:hsql://localhost/"+ConfigurationHelper.getDbname();
	}


}
