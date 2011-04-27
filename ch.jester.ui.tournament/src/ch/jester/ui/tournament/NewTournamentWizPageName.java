package ch.jester.ui.tournament;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.layout.GridData;

public class NewTournamentWizPageName extends WizardPage implements ModifyListener {
	private Text tournamentName;
	
	protected NewTournamentWizPageName() {
		super("Tournament name");
		setTitle("New Tournament");
		setMessage("Creating a new tournament");
		setDescription("Creating a new tournament");
		setPageComplete(false);
	}

	@Override
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);
		setControl(container);
		container.setLayout(new GridLayout(4, false));
		new Label(container, SWT.NONE);
		new Label(container, SWT.NONE);
		new Label(container, SWT.NONE);
		new Label(container, SWT.NONE);
		new Label(container, SWT.NONE);
		
		Label label = new Label(container, SWT.NONE);
		label.setText("Name");
		new Label(container, SWT.NONE);
		
		tournamentName = new Text(container, SWT.BORDER);
		tournamentName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		tournamentName.addModifyListener(this);
	}
	
	/**
	 * Validierung der WizardPage damit die nächste Seite angezeigt werden kann.
	 * @return	true, wenn alle benötigten Angaben gemacht wurden.
	 */
	private boolean validatePage() {
		if (tournamentName.getText().isEmpty()) return false;
		return true;
	}
	
	// Listener Methoden
	@Override
	public void modifyText(ModifyEvent e) {
		setPageComplete(validatePage());
	}

	// Getter und Setter Methoden
	
	public Text getTournamentName() {
		return tournamentName;
	}
}
