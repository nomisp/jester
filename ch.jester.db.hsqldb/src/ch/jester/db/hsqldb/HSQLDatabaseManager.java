package ch.jester.db.hsqldb;

import java.sql.SQLException;

import org.hibernate.HibernateException;
import org.hsqldb.Server;

import ch.jester.hibernate.helper.ConfigurationHelper;
import ch.jester.hibernate.helper.IDatabaseManager;

public class HSQLDatabaseManager implements IDatabaseManager {
	private final static String DB_NAME = "jester";
	private Server server;
	public HSQLDatabaseManager() {
		server = new Server();
	}

	@Override
	public void start() {
		ConfigurationHelper helper = new ConfigurationHelper();
		String ip = helper.getIp();
		server.setDatabaseName(0, DB_NAME);
		server.setDatabasePath(0, ip+"/"+DB_NAME+";hsqldb.default_table_type=cached");  //default_table_type=cached --> damit tables auch auf der platte landen
		server.start();
		
	}

	@Override
	public void stop() {
		server.stop();
	}

	@Override
	public void shutdown() {
		try {
			
			// TODO geht das auch schöner?
			//beendet db richtig... alles andere funktioniert nicht so wirklich
			new ConfigurationHelper().getSession().connection().createStatement().execute("shutdown");
		} catch (HibernateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		server.shutdown();
	}

}
