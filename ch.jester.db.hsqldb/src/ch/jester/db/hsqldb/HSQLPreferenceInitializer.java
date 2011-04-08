package ch.jester.db.hsqldb;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

public class HSQLPreferenceInitializer extends AbstractPreferenceInitializer {

	public HSQLPreferenceInitializer() {
	}

	@Override
	public void initializeDefaultPreferences() {
		IPreferenceStore store = Activator.getDefault().getPreferenceStore();
		// HSQLDB Konfiguration
		store.setDefault("JDBCDriverClass", "org.hsqldb.jdbcDriver");
		store.setDefault("SQLDialectClass", "org.hibernate.dialect.HSQLDialect");
		store.setDefault("Subprotocol", "hsqldb");
		store.setDefault("DatabaseManagerClass", "ch.jester.db.hsqldb.SimpleHSQLDatabaseManager");
		
		store.setDefault("DefaultTableType", "cached");
		store.setDefault("TransactionMode", "mvcc");
		store.setDefault("Ip", "jdbc:hsqldb:hsql://localhost/");
	}
}
