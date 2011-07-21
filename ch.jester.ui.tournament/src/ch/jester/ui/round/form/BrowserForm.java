package ch.jester.ui.round.form;

import java.io.File;

import messages.Messages;

import org.eclipse.jface.action.Action;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.editor.FormPage;
import org.eclipse.ui.forms.widgets.ScrolledForm;

import ch.jester.common.ui.utility.UIUtility;
import ch.jester.ui.round.editors.RankingViewEditor;

public class BrowserForm extends FormPage {
	Browser browser;
	RankingViewEditor edit;

	public BrowserForm(FormEditor editor, String id, String title) {
		super(editor, id, title);
		edit = (RankingViewEditor) editor;
	}
	protected void createToolBarActions(IManagedForm managedForm) {
		Action pdf = new Action("2pdf", Action.AS_PUSH_BUTTON) { //$NON-NLS-1$
			public void run() {
				edit.convert2PDF();
			}
		};
	//	pdf.setEnabled(false);
		pdf.setText(Messages.BrowserForm_btn_pdf);
		managedForm.getForm().getToolBarManager().add(pdf);
		
		Action print = new Action("print", Action.AS_PUSH_BUTTON) { //$NON-NLS-1$
			public void run() {
				edit.toPrinter();
			}
		};
		//print.setEnabled(false);
		print.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_ETOOL_PRINT_EDIT));
		print.setText(Messages.BrowserForm_btn_print);
		managedForm.getForm().getToolBarManager().add(print);
		
		managedForm.getForm().getToolBarManager().update(true);

	}
	
	@Override
	protected void createFormContent(IManagedForm managedForm) {
		ScrolledForm sform = managedForm.getForm();
		sform.setText(Messages.BrowserForm_editor_title);
		managedForm.getToolkit().decorateFormHeading(sform.getForm());
		GridLayout gl = new GridLayout(1, true);
		gl.marginBottom=0;
		gl.marginTop=0;
		gl.marginLeft=0;
		gl.marginRight=0;
		gl.marginHeight=0;
		gl.marginWidth=0;
		managedForm.getForm().getBody().setLayout(gl);
		browser = new Browser(managedForm.getForm().getBody(), SWT.NONE);

		managedForm.getToolkit().adapt(browser);
		GridData gd = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		gd.widthHint = 94;
		gd.grabExcessHorizontalSpace=true;
		gd.grabExcessVerticalSpace=true;
		browser.setLayoutData(gd);
		//browser.setUrl(edit.getInputFile().getAbsolutePath());
		//browser.refresh();
		createToolBarActions(managedForm);
	}

		public void setInput(File tmpFile) {
			UIUtility.asyncExecInUIThread(new Runnable(){

				@Override
				public void run() {
					browser.setUrl(edit.getInputFile().getAbsolutePath());
					browser.refresh();
					
				}
				
				
			});
			
			
		}
		
		public void zoom(int i) {
			
			

			
		}
}
