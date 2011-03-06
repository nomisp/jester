package ch.jester.db.hsqldb;

import org.hsqldb.Server;

import ch.jester.hibernate.helper.IDatabaseManager;

public class HSQLDatabaseManager implements IDatabaseManager {

	private Server server;
	
	public HSQLDatabaseManager() {
		server = new Server();
	}

	@Override
	public void start() {
		if (server == null) server = new Server();
		server.setDatabaseName(0, "jester");
		server.setDatabasePath(0, "file:jesterdb");
		server.start();
	}

	@Override
	public void stop() {
		server.stop();
	}

}
