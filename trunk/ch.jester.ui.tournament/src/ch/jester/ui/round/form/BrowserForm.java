package ch.jester.ui.round.form;

import java.io.File;

import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.editor.FormPage;
import org.eclipse.ui.forms.widgets.ScrolledForm;

import ch.jester.ui.round.editors.RankingViewEditor;

public class BrowserForm extends FormPage{
	//public static final String ID = "ch.jester.ui.tournament.browse"; //$NON-NLS-1$
	Browser browser;
	RankingViewEditor edit;
	public BrowserForm(FormEditor editor, String id, String title) {
		super(editor, id, title);
		edit = (RankingViewEditor) editor;
	}

@Override
	protected void createFormContent(IManagedForm managedForm) {
		ScrolledForm sform = managedForm.getForm();
		managedForm.getForm().getBody().setLayout(new GridLayout(1, false));
		browser = new Browser(managedForm.getForm().getBody(), SWT.NONE);
		managedForm.getToolkit().adapt(browser);
		browser.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		browser.setUrl(edit.getInputFile().getAbsolutePath());
		browser.refresh();
	}

		public void setInput(File tmpFile) {
			
			
		}
}