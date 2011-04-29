package ch.jester.ui.player.editor;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class PlayerDetails extends Composite {

	private PlayerDetailsController m_controller;
	private Text lastNameText;
	private Text firstNameText;
	private Text cityText;
	private Text nationText;
	private Text fideCodeText;
	private Text nationalCodeText;
	private Text eloText;
	private Text nationalEloText;

	public PlayerDetails(Composite parent, int style) {
		super(parent, style);
		setLayout(new GridLayout(2, false));

		new Label(this, SWT.NONE).setText("LastName:");

		lastNameText = new Text(this, SWT.BORDER | SWT.SINGLE);
		lastNameText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false));

		new Label(this, SWT.NONE).setText("FirstName:");

		firstNameText = new Text(this, SWT.BORDER | SWT.SINGLE);
		firstNameText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false));

		new Label(this, SWT.NONE).setText("City:");

		cityText = new Text(this, SWT.BORDER | SWT.SINGLE);
		cityText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

		new Label(this, SWT.NONE).setText("Nation:");

		nationText = new Text(this, SWT.BORDER | SWT.SINGLE);
		nationText
				.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

		new Label(this, SWT.NONE).setText("FideCode:");

		fideCodeText = new Text(this, SWT.BORDER | SWT.SINGLE);
		fideCodeText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false));

		new Label(this, SWT.NONE).setText("NationalCode:");

		nationalCodeText = new Text(this, SWT.BORDER | SWT.SINGLE);
		nationalCodeText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false));

		new Label(this, SWT.NONE).setText("Elo:");

		eloText = new Text(this, SWT.BORDER | SWT.SINGLE);
		eloText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

		new Label(this, SWT.NONE).setText("NationalElo:");

		nationalEloText = new Text(this, SWT.BORDER | SWT.SINGLE);
		nationalEloText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false));

		m_controller = new PlayerDetailsController(this);
	}

	public PlayerDetailsController getController(){
		return m_controller;
	}
	
	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

	public Text getLastNameText() {
		return lastNameText;
	}

	public Text getFirstNameText() {
		return firstNameText;
	}

	public Text getCityText() {
		return cityText;
	}

	public Text getNationText() {
		return nationText;
	}

	public Text getFideCodeText() {
		return fideCodeText;
	}

	public Text getNationalCodeText() {
		return nationalCodeText;
	}

	public Text getEloText() {
		return eloText;
	}

	public Text getNationalEloText() {
		return nationalEloText;
	}

}