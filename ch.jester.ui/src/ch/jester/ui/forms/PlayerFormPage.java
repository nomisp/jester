package ch.jester.ui.forms;

import org.eclipse.swt.SWT;
import org.eclipse.swt.internal.win32.MCHITTESTINFO;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.editor.FormPage;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;

import ch.jester.common.ui.editorutilities.DirtyManager;
import ch.jester.common.ui.editorutilities.IDirtyManagerProvider;
import ch.jester.common.ui.editorutilities.SWTDirtyManager;
import ch.jester.ui.player.editor.PlayerEditor;
import ch.jester.ui.player.editor.ctrl.PlayerDetailsController;

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
		FormToolkit toolkit = managedForm.getToolkit();
		ScrolledForm form = managedForm.getForm();
		form.setText("Player Details");
		Composite body = form.getBody();
		managedForm.getForm().getBody().setLayout(new GridLayout(1, false));
		
		Composite compPersonal = managedForm.getToolkit().createComposite(managedForm.getForm().getBody(), SWT.NONE);
		compPersonal.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 2, 1));
		managedForm.getToolkit().paintBordersFor(compPersonal);
		compPersonal.setLayout(new GridLayout(2, false));
		
		Section sctnPersonal = managedForm.getToolkit().createSection(compPersonal, Section.EXPANDED | Section.LEFT_TEXT_CLIENT_ALIGNMENT | Section.DESCRIPTION | Section.TWISTIE | Section.TITLE_BAR);
		GridData gd_sctnPersonal = new GridData(SWT.FILL, SWT.TOP, true, false, 2, 1);
		gd_sctnPersonal.widthHint = 561;
		sctnPersonal.setLayoutData(gd_sctnPersonal);
		managedForm.getToolkit().paintBordersFor(sctnPersonal);
		sctnPersonal.setText("Personal data");
		
		Composite composite_2 = managedForm.getToolkit().createComposite(sctnPersonal, SWT.NONE);
		managedForm.getToolkit().paintBordersFor(composite_2);
		sctnPersonal.setClient(composite_2);
		composite_2.setLayout(new GridLayout(2, false));
		
		Label lblNewLabel = managedForm.getToolkit().createLabel(composite_2, "Lastname", SWT.NONE);
		
		lastNameText = managedForm.getToolkit().createText(composite_2, "New Text", SWT.NONE);
		lastNameText.setText("");
		GridData gd_txtLastname = new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1);
		gd_txtLastname.widthHint = 315;
		lastNameText.setLayoutData(gd_txtLastname);
		
		Label lblNewLabel_2 = managedForm.getToolkit().createLabel(composite_2, "Firstname", SWT.NONE);
		
		firstNameText = managedForm.getToolkit().createText(composite_2, "New Text", SWT.NONE);
		firstNameText.setText("");
		firstNameText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblNewLabel_3 = managedForm.getToolkit().createLabel(composite_2, "Age", SWT.NONE);
		
		txtAge = managedForm.getToolkit().createText(composite_2, "New Text", SWT.NONE);
		txtAge.setText("");
		txtAge.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblNewLabel_4 = managedForm.getToolkit().createLabel(composite_2, "City", SWT.NONE);
		
		cityText = managedForm.getToolkit().createText(composite_2, "New Text", SWT.NONE);
		cityText.setText("");
		cityText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblNation = managedForm.getToolkit().createLabel(composite_2, "Nation", SWT.NONE);
		
		nationText = managedForm.getToolkit().createText(composite_2, "New Text", SWT.NONE);
		nationText.setText("");
		nationText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		
		Composite compChess = managedForm.getToolkit().createComposite(managedForm.getForm().getBody(), SWT.NONE);
		compChess.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1));
		managedForm.getToolkit().adapt(compChess);
		managedForm.getToolkit().paintBordersFor(compChess);
		compChess.setLayout(new GridLayout(1, false));
		
		Section sctnChess = managedForm.getToolkit().createSection(compChess, Section.TWISTIE | Section.TITLE_BAR);
		sctnChess.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1));
		managedForm.getToolkit().paintBordersFor(sctnChess);
		sctnChess.setText("Chess data");
		sctnChess.setExpanded(true);
		
		Composite composite_3 = managedForm.getToolkit().createComposite(sctnChess, SWT.NONE);
		managedForm.getToolkit().paintBordersFor(composite_3);
		sctnChess.setClient(composite_3);
		composite_3.setLayout(new GridLayout(2, false));
		
		Label lblNewLabel_7 = managedForm.getToolkit().createLabel(composite_3, "Title", SWT.NONE);
		
		txtTitle = managedForm.getToolkit().createText(composite_3, "New Text", SWT.NONE);
		txtTitle.setText("");
		txtTitle.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblNewLabel_1 = managedForm.getToolkit().createLabel(composite_3, "Fide code", SWT.NONE);
		
		fideCodeText = managedForm.getToolkit().createText(composite_3, "New Text", SWT.NONE);
		fideCodeText.setText("");
		fideCodeText.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1));
		
		Label lblNewLabel_5 = managedForm.getToolkit().createLabel(composite_3, "Naional code", SWT.NONE);
		lblNewLabel_5.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		
		nationalCodeText = managedForm.getToolkit().createText(composite_3, "New Text", SWT.NONE);
		nationalCodeText.setText("");
		nationalCodeText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblNewLabel_6 = managedForm.getToolkit().createLabel(composite_3, "Elo", SWT.NONE);
		
		eloText = managedForm.getToolkit().createText(composite_3, "New Text", SWT.NONE);
		eloText.setText("");
		eloText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblNationalElo = managedForm.getToolkit().createLabel(composite_3, "National Elo", SWT.NONE);
		
		nationalEloText = managedForm.getToolkit().createText(composite_3, "New Text", SWT.NONE);
		nationalEloText.setText("");
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
