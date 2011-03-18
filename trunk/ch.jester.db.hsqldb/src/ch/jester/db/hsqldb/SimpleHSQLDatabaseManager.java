package ch.jester.db.hsqldb;

import java.sql.SQLException;

import org.hibernate.HibernateException;

import ch.jester.hibernate.helper.ConfigurationHelper;
import ch.jester.hibernate.helper.HibernatehelperPlugin;
import ch.jester.hibernate.helper.IDatabaseManager;

public class SimpleHSQLDatabaseManager implements IDatabaseManager {
	private String mDbOptions = ";hsqldb.default_table_type=cached;hsqldb.tx=mvcc";
	public SimpleHSQLDatabaseManager() {
	}

	@Override
	public void start() {

	}

	@SuppressWarnings("deprecation")
	@Override
	public void stop() {
		try {

			// TODO geht das auch schöner?
			// beendet db richtig... alles andere funktioniert nicht so wirklich
			HibernatehelperPlugin.getSession().connection().createStatement()
					.execute("shutdown");
		} catch (HibernateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
		return ConfigurationHelper.getLocalConnection()+mDbOptions;
	}

}
