package ch.jester.db.hsqldb;

import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

public class HSQLPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	public HSQLPreferencePage() {
		super(GRID);
	}
	
	@Override
	public void init(IWorkbench workbench) {
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
		String text = Platform.getPreferencesService().getString("ch.jester.orm", "Database", "ch.jester.db.hsqldb", null);
		System.out.println(text);
	}

	@Override
	protected void createFieldEditors() {
		StringFieldEditor defTableTypeEditor = new StringFieldEditor("DefaultTableType", "Table-Type:", getFieldEditorParent());
		defTableTypeEditor.setStringValue(Platform.getPreferencesService().getString("ch.jester.db.hsqldb", "DefaultTableType", "cached", null));
		defTableTypeEditor.setEnabled(false, getFieldEditorParent());
		addField(defTableTypeEditor);

		StringFieldEditor transactionModeEditor = new StringFieldEditor("TransactionMode", "Transaktionsmodus:", getFieldEditorParent());
		transactionModeEditor.setStringValue(Platform.getPreferencesService().getString("ch.jester.db.hsqldb", "TransactionMode", "mvcc", null));
		transactionModeEditor.setEnabled(false, getFieldEditorParent());
		addField(transactionModeEditor);
		
		StringFieldEditor ipEditor = new StringFieldEditor("Ip", "Ip-Adresse:", getFieldEditorParent());
		ipEditor.setStringValue(Platform.getPreferencesService().getString("ch.jester.db.hsqldb", "Ip", "localhost", null));
		ipEditor.setEnabled(false, getFieldEditorParent());
		addField(ipEditor);
	}

}
