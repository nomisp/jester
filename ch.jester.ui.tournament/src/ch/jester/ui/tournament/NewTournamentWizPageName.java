package ch.jester.ui.tournament;

import java.util.Calendar;
import java.util.Date;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.DateTime;
//import org.eclipse.nebula.widgets.formattedtext.FormattedText;
//import org.eclipse.nebula.widgets.formattedtext.NumberFormatter;
//import java.util.Locale;

import ch.jester.ui.tournament.nl1.Messages;

public class NewTournamentWizPageName extends WizardPage implements ModifyListener {
	private Text tournamentName;
	private Text description;
	private DateTime dateFrom;
	private DateTime dateTo;
	
	protected NewTournamentWizPageName() {
		super("Tournament name"); //$NON-NLS-1$
		setTitle(Messages.NewTournamentWizPageName_title);
		setMessage(Messages.NewTournamentWizPageName_message);
		setDescription(Messages.NewTournamentWizPageName_description);
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
		lblName.setText(Messages.NewTournamentWizPageName_lbl_name);
		new Label(container, SWT.NONE);
		
		tournamentName = new Text(container, SWT.BORDER);
		tournamentName.setFocus();
		tournamentName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		tournamentName.addModifyListener(this);
		new Label(container, SWT.NONE);
		
		Label lblDescription = new Label(container, SWT.NONE);
		lblDescription.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1));
		lblDescription.setText(Messages.NewTournamentWizPageName_lbl_description);
		new Label(container, SWT.NONE);
		
		description = new Text(container, SWT.BORDER | SWT.WRAP);
		GridData gd_description = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd_description.heightHint = 52;
		description.setLayoutData(gd_description);
		new Label(container, SWT.NONE);
		
		Label lblDateFrom = new Label(container, SWT.NONE);
		lblDateFrom.setText(Messages.NewTournamentWizPageName_lbl_dateFrom);
		new Label(container, SWT.NONE);
		
		dateFrom = new DateTime(container, SWT.BORDER);
		new Label(container, SWT.NONE);
		
		Label lblDateTo = new Label(container, SWT.NONE);
		lblDateTo.setText(Messages.NewTournamentWizPageName_lbl_dateTo);
		new Label(container, SWT.NONE);
		
		dateTo = new DateTime(container, SWT.BORDER);
	}
	
	@Override
	public boolean canFlipToNextPage(){
	   if (getErrorMessage() != null) return false;
	   return validatePage();
	}
	
	/**
	 * Validierung der WizardPage damit die nächste Seite angezeigt werden kann.
	 * @return	true, wenn alle benötigten Angaben gemacht wurden.
	 */
	private boolean validatePage() {
		if (tournamentName.getText().isEmpty()) return false;
//		setPageComplete(true);
		return true;
	}
	
	// Listener Methoden
	@Override
	public void modifyText(ModifyEvent e) {
		setPageComplete(validatePage());
		getWizard().getContainer().updateButtons();
	}

	// Getter und Setter Methoden
	
	public String getTournamentName() {
		return tournamentName.getText();
	}
	
	public String getDescription() {
		return description.getText();
	}
	
	/**
	 * Gibt das Datum an dem das Turnier beginnt
	 * @return
	 */
	public Date getDateFrom() {
		Calendar cal = Calendar.getInstance();
		cal.set(dateFrom.getYear(), dateFrom.getMonth(), dateFrom.getDay());
		return cal.getTime();
	}
	
	/**
	 * Gibt das Datum an dem das Turnier zu Ende ist
	 * @return
	 */
	public Date getDateTo() {
		Calendar cal = Calendar.getInstance();
		cal.set(dateTo.getYear(), dateTo.getMonth(), dateTo.getDay());
		return cal.getTime();
	}
}
