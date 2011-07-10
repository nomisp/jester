package ch.jester.ui.forms;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.editor.FormPage;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;

import ch.jester.common.ui.editorutilities.DirtyManager;
import ch.jester.common.ui.editorutilities.IDirtyManagerProvider;
import ch.jester.common.ui.editorutilities.SWTDirtyManager;
import ch.jester.ui.nl1.Messages;
import ch.jester.ui.player.editor.PlayerEditor;
import ch.jester.ui.player.editor.ctrl.PlayerDetailsController;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.jface.viewers.ComboViewer;

public class PlayerFormPage extends FormPage implements IDirtyManagerProvider {
	private Text lastNameText;
	private Text fideCodeText;
	private Text firstNameText;
	private Text txtAge;
	private Text cityText;
	private Text nationText;
	private Text nationalCodeText;
	private Text eloText;
	private Text txtTitle;
	private PlayerDetailsController m_controller;
	private SWTDirtyManager dm = new SWTDirtyManager();
	private Text nationalEloText;
	private Label lblClub;
	private Combo combo;
	private ComboViewer comboViewer;
	/**
	 * Create the form page.
	 * @param id
	 * @param title
	 */
	public PlayerFormPage(String id, String title) {
		super(id, title);
	}

	/**
	 * Create the form page.
	 * @param editor
	 * @param id
	 * @param title
	 * @wbp.parser.constructor
	 * @wbp.eval.method.parameter id "Some id"
	 * @wbp.eval.method.parameter title "Some title"
	 */
	public PlayerFormPage(FormEditor editor, String id, String title) {
		super(editor, id, title);
	}

	/**
	 * Create contents of the form.
	 * @param managedForm
	 */
	@Override
	protected void createFormContent(IManagedForm managedForm) {

		ScrolledForm form = managedForm.getForm();
		form.setText(Messages.PlayerFormPage_lbl_player_det);
		managedForm.getForm().getBody().setLayout(new GridLayout(2, false));
		managedForm.getToolkit().decorateFormHeading(form.getForm());
		
		Composite compPersonal = managedForm.getToolkit().createComposite(managedForm.getForm().getBody(), SWT.NONE);
		compPersonal.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 2, 1));
		managedForm.getToolkit().paintBordersFor(compPersonal);
		compPersonal.setLayout(new GridLayout(2, false));
		
		Section sctnPersonal = managedForm.getToolkit().createSection(compPersonal, Section.EXPANDED | Section.TWISTIE | Section.TITLE_BAR);
		GridData gd_sctnPersonal = new GridData(SWT.FILL, SWT.TOP, true, false, 2, 1);
		gd_sctnPersonal.widthHint = 561;
		sctnPersonal.setLayoutData(gd_sctnPersonal);
		managedForm.getToolkit().paintBordersFor(sctnPersonal);
		sctnPersonal.setText(Messages.PlayerFormPage_lbl_personal_data);
		
		Composite composite_2 = managedForm.getToolkit().createComposite(sctnPersonal, SWT.NONE);
		managedForm.getToolkit().paintBordersFor(composite_2);
		sctnPersonal.setClient(composite_2);
		composite_2.setLayout(new GridLayout(2, false));
		
		Label lblNewLabel = managedForm.getToolkit().createLabel(composite_2, Messages.PlayerFormPage_lbl_lastname, SWT.NONE);
		GridData gd_lblNewLabel = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_lblNewLabel.widthHint = 100;
		lblNewLabel.setLayoutData(gd_lblNewLabel);
		
		lastNameText = managedForm.getToolkit().createText(composite_2, "New Text", SWT.NONE); //$NON-NLS-1$
		lastNameText.setText(""); //$NON-NLS-1$
		GridData gd_txtLastname = new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1);
		gd_txtLastname.widthHint = 315;
		lastNameText.setLayoutData(gd_txtLastname);
		
		managedForm.getToolkit().createLabel(composite_2, Messages.PlayerFormPage_lbl_firstname, SWT.NONE);
		
		firstNameText = managedForm.getToolkit().createText(composite_2, "New Text", SWT.NONE); //$NON-NLS-1$
		firstNameText.setText(""); //$NON-NLS-1$
		firstNameText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		managedForm.getToolkit().createLabel(composite_2, Messages.PlayerFormPage_lbl_age, SWT.NONE);
		
		txtAge = managedForm.getToolkit().createText(composite_2, "New Text", SWT.NONE); //$NON-NLS-1$
		txtAge.setText(""); //$NON-NLS-1$
		txtAge.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		managedForm.getToolkit().createLabel(composite_2, Messages.PlayerFormPage_lbl_city, SWT.NONE);
		
		cityText = managedForm.getToolkit().createText(composite_2, "New Text", SWT.NONE); //$NON-NLS-1$
		cityText.setText(""); //$NON-NLS-1$
		cityText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		managedForm.getToolkit().createLabel(composite_2, Messages.PlayerFormPage_lbl_nation, SWT.NONE);
		
		nationText = managedForm.getToolkit().createText(composite_2, "New Text", SWT.NONE); //$NON-NLS-1$
		nationText.setText(""); //$NON-NLS-1$
		nationText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		
		Composite compChess = managedForm.getToolkit().createComposite(managedForm.getForm().getBody(), SWT.NONE);
		compChess.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1));
		managedForm.getToolkit().adapt(compChess);
		managedForm.getToolkit().paintBordersFor(compChess);
		compChess.setLayout(new GridLayout(1, false));
		
		Section sctnChess = managedForm.getToolkit().createSection(compChess, Section.EXPANDED | Section.TWISTIE | Section.TITLE_BAR);
		sctnChess.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1));
		managedForm.getToolkit().paintBordersFor(sctnChess);
		sctnChess.setText(Messages.PlayerFormPage_lbl_chess_data);
		sctnChess.setExpanded(true);
		
		Composite composite_3 = managedForm.getToolkit().createComposite(sctnChess, SWT.NONE);
		managedForm.getToolkit().paintBordersFor(composite_3);
		sctnChess.setClient(composite_3);
		composite_3.setLayout(new GridLayout(2, false));
		
		Label lblNewLabel_7 = managedForm.getToolkit().createLabel(composite_3, Messages.PlayerFormPage_lbl_title, SWT.NONE);
		GridData gd_lblNewLabel_7 = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_lblNewLabel_7.widthHint = 100;
		lblNewLabel_7.setLayoutData(gd_lblNewLabel_7);
		
		txtTitle = managedForm.getToolkit().createText(composite_3, "New Text", SWT.NONE); //$NON-NLS-1$
		txtTitle.setText(""); //$NON-NLS-1$
		txtTitle.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		managedForm.getToolkit().createLabel(composite_3, Messages.PlayerFormPage_lbl_fidecode, SWT.NONE);
		
		fideCodeText = managedForm.getToolkit().createText(composite_3, "New Text", SWT.NONE); //$NON-NLS-1$
		fideCodeText.setText(""); //$NON-NLS-1$
		fideCodeText.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1));
		
		managedForm.getToolkit().createLabel(composite_3, Messages.PlayerFormPage_lbl_nationalcode, SWT.NONE);
		
		nationalCodeText = managedForm.getToolkit().createText(composite_3, "New Text", SWT.NONE); //$NON-NLS-1$
		nationalCodeText.setText(""); //$NON-NLS-1$
		nationalCodeText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		managedForm.getToolkit().createLabel(composite_3, Messages.PlayerFormPage_lbl_elo, SWT.NONE);
		
		eloText = managedForm.getToolkit().createText(composite_3, "New Text", SWT.NONE); //$NON-NLS-1$
		eloText.setText(""); //$NON-NLS-1$
		eloText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		managedForm.getToolkit().createLabel(composite_3, Messages.PlayerFormPage_lbl_nationalelo, SWT.NONE);
		
		nationalEloText = managedForm.getToolkit().createText(composite_3, "New Text", SWT.NONE); //$NON-NLS-1$
		nationalEloText.setText(""); //$NON-NLS-1$
		nationalEloText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
	
		
		
		dm.add(lastNameText,
				fideCodeText,
				firstNameText,
				txtAge,
				cityText,
				nationText,
				nationalCodeText,
				eloText,
				txtTitle, 
				nationalEloText);
		
		lblClub = new Label(composite_3, SWT.NONE);
		managedForm.getToolkit().adapt(lblClub, true, true);
		lblClub.setText(Messages.PlayerFormPage_lbl_club);
		
		comboViewer = new ComboViewer(composite_3, SWT.NONE|SWT.READ_ONLY);
		
		combo = comboViewer.getCombo();
		combo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		managedForm.getToolkit().paintBordersFor(combo);
		new Label(managedForm.getForm().getBody(), SWT.NONE);
		m_controller = new PlayerDetailsController(this);
		((PlayerEditor)getEditor()).init_0(this);
		
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

	@Override
	public DirtyManager getDirtyManager() {
		return this.dm;
	}

	public PlayerDetailsController getController() {
		return m_controller;
	}
}
