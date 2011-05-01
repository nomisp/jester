package ch.jester.h2;

import java.sql.SQLException;

import ch.jester.orm.IDatabaseManager;
import ch.jester.orm.ORMPlugin;


public class SimpleH2DatabaseManager implements IDatabaseManager {
	private String mDbOptions = "";
	public SimpleH2DatabaseManager() {

	}
	@Override
	public void start() {
	
	}


	@Override
	public void stop() {
		try {

			// TODO geht das auch schöner?
			// beendet db richtig... alles andere funktioniert nicht so wirklich
			ORMPlugin.getConfiguration().getConnection().createStatement()
					.execute("shutdown");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void shutdown() {

	}

	@Override
	public String getIP() {
		return ORMPlugin.getConfiguration().getLocalConnection()+mDbOptions;
	}

}
