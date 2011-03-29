package ch.jester.orm;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

public class DBPreferenceInitializer extends AbstractPreferenceInitializer {

	public DBPreferenceInitializer() {
	}

	@Override
	public void initializeDefaultPreferences() {
		IPreferenceStore store = ORMPlugin.getDefault().getPreferenceStore();
		store.setDefault("Database", "ch.jester.db.hsqldb");
	}

}
