package ch.jester.orm;

import org.eclipse.jface.preference.ComboFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

public class DatabasePreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {
	
	public DatabasePreferencePage() {
		super(GRID);
	}
	
	@Override
	public void init(IWorkbench workbench) {
		setPreferenceStore(ORMPlugin.getDefault().getPreferenceStore());
		setDescription("Database Preferences");
	}

	@Override
	protected void createFieldEditors() {
		addField(new ComboFieldEditor("Database", "Database: ", new String[][] { { "HSQLDB", "ch.jester.db.hsqldb" },
				{ "C&hoice 2", "choice2" } }, getFieldEditorParent()));
	}

}
