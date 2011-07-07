package ch.jester.ui.round.form;

import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.editor.FormPage;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.GridData;

public class BrowserForm extends FormPage{

	public BrowserForm(FormEditor editor, String id, String title) {
		super(editor, id, title);
	}

@Override
	protected void createFormContent(IManagedForm managedForm) {
		ScrolledForm sform = managedForm.getForm();
		managedForm.getForm().getBody().setLayout(new GridLayout(1, false));
		Browser browser = new Browser(managedForm.getForm().getBody(), SWT.NONE);
		browser.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
	}
}
