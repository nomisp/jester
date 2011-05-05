package ch.jester.orm.internal;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

import ch.jester.orm.ORMPlugin;

public class DBPreferenceInitializer extends AbstractPreferenceInitializer {

	public DBPreferenceInitializer() {
	}

	@Override
	public void initializeDefaultPreferences() {
		IPreferenceStore store = ORMPlugin.getDefault().getPreferenceStore();
		store.setDefault("Database", "ch.jester.db.hsqldb");
		store.setDefault("DatabaseName", "jester");
		store.setDefault("ORMConfiguration", "ch.jester.hibernate.ConfigurationHelper");
	}

}
