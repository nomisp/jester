package ch.jester.orm.internal;

import org.eclipse.jface.preference.ComboFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import ch.jester.commonservices.api.logging.ILogger;
import ch.jester.orm.ORMPlugin;

public class DatabasePreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {
	
	private ILogger logger;

	public DatabasePreferencePage() {
		super(GRID);
		logger = ORMPlugin.getDefault().getActivationContext().getLogger();
	}
	
	@Override
	public void init(IWorkbench workbench) {
		setPreferenceStore(ORMPlugin.getDefault().getPreferenceStore());
		setDescription(ORMMessages.DatabasePreferencePage_Description);
	}

	@Override
	protected void createFieldEditors() {
		IPreferenceStore preferenceStore = ORMPlugin.getDefault().getPreferenceStore();
		String databasePlugin = preferenceStore.getString("Database");	// Konkretes Datenbankplugin aus welchem die Konfiguration gelesen werden soll.
		
		logger.info("Selected Database: " + databasePlugin);
		
		String[][] bundleNames = ORMDBUtil.getBundleName(ORMDBUtil.getDataBasePlugins());
		addField(new ComboFieldEditor(ORMAutoDBHandler.DEFAULT_DATABASE, ORMMessages.DatabasePreferencePage_DatabaseLabel, bundleNames, getFieldEditorParent())); //$NON-NLS-1$ //$NON-NLS-2$
		
	}


}
