package ch.jester.ui.tournament;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.GridData;

public class CategoryDialog extends Dialog {
	private String dialogTitle = "Category";
	private Text description;
	private Text eloMin;
	private Text eloMax;
	private Text ageMin;
	private Text ageMax;
	private Text maxRounds;

	/**
	 * Create the dialog.
	 * @param parentShell
	 */
	public CategoryDialog(Shell parentShell, boolean newDialog) {
		super(parentShell);
		if (newDialog) {
			dialogTitle = "New Category";
		} else {
			// TODO Peter: Felder müssen befüllt sein mit den alten Daten -> Edit
		}
	}
	
	@Override
	protected void configureShell(Shell shell) {
		super.configureShell(shell);
		shell.setText(dialogTitle);
    }

	/**
	 * Create contents of the dialog.
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		GridLayout gridLayout = (GridLayout) container.getLayout();
		gridLayout.numColumns = 3;
		
		Label lblDescription = new Label(container, SWT.NONE);
		lblDescription.setText("Description");
		
		description = new Text(container, SWT.BORDER);
		description.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		new Label(container, SWT.NONE);
		
		Label lblMin = new Label(container, SWT.NONE);
		GridData gd_lblMin = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_lblMin.widthHint = 172;
		lblMin.setLayoutData(gd_lblMin);
		lblMin.setText("Min.");
		
		Label lblMax = new Label(container, SWT.NONE);
		lblMax.setText("Max.");
		
		Label lblEloRange = new Label(container, SWT.NONE);
		lblEloRange.setText("Elo Range");
		
		eloMin = new Text(container, SWT.BORDER);
		GridData gd_eloMin = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd_eloMin.widthHint = 44;
		eloMin.setLayoutData(gd_eloMin);
		
		eloMax = new Text(container, SWT.BORDER);
		GridData gd_eloMax = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd_eloMax.widthHint = 145;
		eloMax.setLayoutData(gd_eloMax);
		
		Label lblAge = new Label(container, SWT.NONE);
		lblAge.setText("Age");
		
		ageMin = new Text(container, SWT.BORDER);
		ageMin.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		ageMax = new Text(container, SWT.BORDER);
		GridData gd_ageMax = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd_ageMax.widthHint = 176;
		ageMax.setLayoutData(gd_ageMax);
		
		Label lblMaxRounds = new Label(container, SWT.NONE);
		lblMaxRounds.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblMaxRounds.setToolTipText("Max number of rounds");
		lblMaxRounds.setText("Max rounds");
		new Label(container, SWT.NONE);
		
		maxRounds = new Text(container, SWT.BORDER);
		maxRounds.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		return container;
	}

	/**
	 * Create contents of the button bar.
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL,
				true);
		createButton(parent, IDialogConstants.CANCEL_ID,
				IDialogConstants.CANCEL_LABEL, false);
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(450, 300);
	}

	public String getDescription() {
		return description.getText();
	}

	public void setDescription(String description) {
		this.description.setText(description);
	}

	public int getEloMin() {
		return Integer.parseInt(eloMin.getText());
	}

	public void setEloMin(String eloMin) {
		this.eloMin.setText(eloMin);
	}

	public int getEloMax() {
		return Integer.parseInt(eloMax.getText());
	}

	public void setEloMax(String eloMax) {
		this.eloMax.setText(eloMax);
	}

	public int getAgeMin() {
		return Integer.parseInt(ageMin.getText());
	}

	public void setAgeMin(String ageMin) {
		this.ageMin.setText(ageMin);
	}

	public int getAgeMax() {
		return Integer.parseInt(ageMax.getText());
	}

	public void setAgeMax(String ageMax) {
		this.ageMax.setText(ageMax);
	}

	public int getMaxRounds() {
		return Integer.parseInt(maxRounds.getText());
	}

	public void setMaxRounds(String maxRounds) {
		this.maxRounds.setText(maxRounds);
	}	
}
