package ch.jester.commonservices.impl.web;

import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import ch.jester.commonservices.impl.internal.Activator;




public class HTTPProxyPreferencePage extends FieldEditorPreferencePage implements
		IWorkbenchPreferencePage {

	public HTTPProxyPreferencePage() {
		super(GRID);
		

	}

	public void createFieldEditors() {
		addField(new StringFieldEditor("proxy.address", "Proxy Address:",
				getFieldEditorParent()));
		addField(new StringFieldEditor("proxy.port", "Proxy Port:",
				getFieldEditorParent()));
	}

	@Override
	public void init(IWorkbench workbench) {
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
	}
}

