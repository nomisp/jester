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
import org.eclipse.jface.fieldassist.ControlDecoration;
import org.eclipse.swt.widgets.DateTime;
//import org.eclipse.nebula.widgets.formattedtext.FormattedText;
//import org.eclipse.nebula.widgets.formattedtext.NumberFormatter;
//import java.util.Locale;

public class NewTournamentWizPageName extends WizardPage implements ModifyListener {
	private Text tournamentName;
	private Text description;
	private Text year;
	
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
		
		Label lblName = new Label(container, SWT.NONE);
		lblName.setText("Name");
		new Label(container, SWT.NONE);
		
		tournamentName = new Text(container, SWT.BORDER);
		tournamentName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		tournamentName.addModifyListener(this);
		new Label(container, SWT.NONE);
		
		Label lblDescription = new Label(container, SWT.NONE);
		lblDescription.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1));
		lblDescription.setText("Description");
		new Label(container, SWT.NONE);
		
		description = new Text(container, SWT.BORDER | SWT.WRAP);
		GridData gd_description = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd_description.heightHint = 52;
		description.setLayoutData(gd_description);
		new Label(container, SWT.NONE);
		
		Label lblYear = new Label(container, SWT.NONE);
		lblYear.setText("Year");
		new Label(container, SWT.NONE);
		
//		FormattedText formattedText = new FormattedText(container, SWT.NONE);
//		formattedText.setFormatter(new NumberFormatter(null, null, Locale.getDefault()));
//		year = formattedText.getControl();
		year = new Text(container, SWT.BORDER);
		year.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		new Label(container, SWT.NONE);
		
		Label lblDateFrom = new Label(container, SWT.NONE);
		lblDateFrom.setText("Date From");
		new Label(container, SWT.NONE);
		
		DateTime dateFrom = new DateTime(container, SWT.BORDER);
		new Label(container, SWT.NONE);
		
		Label lblDateTo = new Label(container, SWT.NONE);
		lblDateTo.setText("Date To");
		new Label(container, SWT.NONE);
		
		DateTime dateTo = new DateTime(container, SWT.BORDER);
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
