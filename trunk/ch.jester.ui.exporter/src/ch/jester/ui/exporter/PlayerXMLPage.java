package ch.jester.ui.exporter;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Button;

public class PlayerXMLPage extends WizardPage {
	private Text text;

	/**
	 * Create the wizard.
	 */
	public PlayerXMLPage() {
		super("wizardPage");
		setTitle("Wizard Page title");
		setDescription("Wizard Page description");
	}

	/**
	 * Create contents of the wizard.
	 * @param parent
	 */
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);

		setControl(container);
		
		text = new Text(container, SWT.BORDER);
		text.setBounds(28, 35, 284, 21);
		
		Button btnNewButton = new Button(container, SWT.NONE);
		btnNewButton.setBounds(341, 35, 75, 25);
		btnNewButton.setText("New Button");
		
		btnNewButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				FileDialog fd = new FileDialog(getShell(), SWT.SAVE);
		        fd.setText("Open");
		        fd.setFilterPath(".");
		        String[] filterExt = { "*.zip", "*.*" };
		        fd.setFilterExtensions(filterExt);
		        String selected = fd.open();
		        if(selected==null){return;}
		        text.setText(selected);

		        
			}


		});
	}
	public String getFileName(){
		return text.getText();
	}
}
