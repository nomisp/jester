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
		setDescription(ORMMessages.DatabasePreferencePage_Description);
	}

	@Override
	protected void createFieldEditors() {
		addField(new ComboFieldEditor("Database", ORMMessages.DatabasePreferencePage_DatabaseLabel, new String[][] { { "HSQLDB", "ch.jester.db.hsqldb" }, //$NON-NLS-1$ //$NON-NLS-3$ //$NON-NLS-4$
				{ "C&hoice 2", "choice2" } }, getFieldEditorParent())); //$NON-NLS-1$ //$NON-NLS-2$
	}

}
