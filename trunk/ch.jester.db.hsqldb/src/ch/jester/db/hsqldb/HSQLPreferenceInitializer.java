package ch.jester.db.hsqldb;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

public class HSQLPreferenceInitializer extends AbstractPreferenceInitializer {

	public HSQLPreferenceInitializer() {
	}

	@Override
	public void initializeDefaultPreferences() {
		IPreferenceStore store = Activator.getDefault().getPreferenceStore();
//		store.setDefault("MySTRING1", "http://www.vogella.de");
	}
}
