package ch.jester.orm;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.List;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IContributor;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.preference.ComboFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.osgi.framework.Bundle;

import ch.jester.common.utility.ExtensionPointUtil;
import ch.jester.commonservices.api.logging.ILogger;

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
		
		String[][] bundleNames = getBundleName(getDataBasePlugins());
			//addField(new ComboFieldEditor("Database", ORMMessages.DatabasePreferencePage_DatabaseLabel, new String[][] { { "HSQLDB", "ch.jester.db.hsqldb" }, //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-2$
			//		{ "C&hoice 2", "choice2" } }, getFieldEditorParent())); //$NON-NLS-1$ //$NON-NLS-2$
			addField(new ComboFieldEditor("Database", ORMMessages.DatabasePreferencePage_DatabaseLabel, bundleNames, getFieldEditorParent())); //$NON-NLS-1$ //$NON-NLS-2$
		StringFieldEditor dbNameEditor = new StringFieldEditor("DatabaseName", ORMMessages.DatabasePreferencePage_DatabaseName, getFieldEditorParent()); //$NON-NLS-1$
		dbNameEditor.setStringValue(preferenceStore.getString("DatabaseName"));
		addField(dbNameEditor);
		
		String jdbcDriver = Platform.getPreferencesService().getString(databasePlugin, "JDBCDriverClass", "org.hsqldb.jdbcDriver", null);
		StringFieldEditor jdbcDriverEditor = new StringFieldEditor("JDBCDriverClass", ORMMessages.DatabasePreferencePage_JDBCDriver, getFieldEditorParent()); //$NON-NLS-1$
		jdbcDriverEditor.setStringValue(jdbcDriver);
		jdbcDriverEditor.setEnabled(false, getFieldEditorParent());
		addField(jdbcDriverEditor);
		
		String sqlDialect = Platform.getPreferencesService().getString(databasePlugin, "SQLDialectClass", "org.hibernate.dialect.HSQLDialect", null);
		StringFieldEditor sqlDialectEditor = new StringFieldEditor("SQLDialectClass", ORMMessages.DatabasePreferencePage_SQLDialect, getFieldEditorParent()); //$NON-NLS-1$
		sqlDialectEditor.setStringValue(sqlDialect);
		sqlDialectEditor.setEnabled(false, getFieldEditorParent());
		addField(sqlDialectEditor);
		
		String subProtocol = Platform.getPreferencesService().getString(databasePlugin, "Subprotocol", "hsqldb", null);
		StringFieldEditor subProtocolEditor = new StringFieldEditor("Subprotocol", ORMMessages.DatabasePreferencePage_Subprotocol, getFieldEditorParent()); //$NON-NLS-1$
		subProtocolEditor.setStringValue(subProtocol);
		subProtocolEditor.setEnabled(false, getFieldEditorParent());
		addField(subProtocolEditor);
		
//		addField(new StringFieldEditor("DatabaseManagerClass", ORMMessages.DatabasePreferencePage_DatabaseManager, //$NON-NLS-1$
//				getFieldEditorParent()));
//		addField(new StringFieldEditor("ORMConfiguration", ORMMessages.DatabasePreferencePage_ORMConfiguration, //$NON-NLS-1$
//				getFieldEditorParent()));
	}

	private List<Bundle> getDataBasePlugins(){
		IConfigurationElement[] elements = ExtensionPointUtil.getExtensionPointElements(ORMPlugin.getDefault().getActivationContext().getPluginId(), "Configuration");
		List<Bundle> bundles = new ArrayList<Bundle>();
		for(IConfigurationElement e:elements){
			IContributor contributor = e.getContributor();
			Bundle b = Platform.getBundle(contributor.getName());
			String name = b.getHeaders().get("Bundle-Name").toString();
			logger.debug("Available DB Plugins: "+b.getSymbolicName()+" ("+name+")");
			bundles.add(b);
		}
		return bundles;
	}
	private String[][] getBundleName(List<Bundle> pList){
		String[][] names = new String[pList.size()][2];
		for(int i=0;i<pList.size();i++){
			names[i][0]= pList.get(i).getHeaders().get("Bundle-Name").toString();
			names[i][1] = pList.get(i).getSymbolicName();
		}
		return names;
	}
}
