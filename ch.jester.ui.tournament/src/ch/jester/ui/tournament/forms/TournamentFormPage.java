package ch.jester.ui.tournament.forms;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.editor.FormPage;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;

import ch.jester.common.ui.editorutilities.DirtyManager;
import ch.jester.common.ui.editorutilities.IDirtyManagerProvider;
import ch.jester.common.ui.editorutilities.SWTDirtyManager;
import ch.jester.ui.tournament.ctrl.TournamentDetailsController;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.swt.widgets.Text;

public class TournamentFormPage extends FormPage implements IDirtyManagerProvider {

	private TournamentDetailsController mController;
	private SWTDirtyManager dm = new SWTDirtyManager();
	private Text nameText;
	private Text descriptionText;
	
	/**
	 * Create the form page.
	 * @param id
	 * @param title
	 */
	public TournamentFormPage(String id, String title) {
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
	public TournamentFormPage(FormEditor editor, String id, String title) {
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
		form.setText("TournamentEditor");
		Composite body = form.getBody();
		toolkit.decorateFormHeading(form.getForm());
		toolkit.paintBordersFor(body);
		managedForm.getForm().getBody().setLayout(new GridLayout(1, false));
		
		Composite compTournament = managedForm.getToolkit().createComposite(managedForm.getForm().getBody(), SWT.NONE);
		GridData gd_compTournament = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_compTournament.widthHint = 584;
		gd_compTournament.heightHint = 212;
		compTournament.setLayoutData(gd_compTournament);
		managedForm.getToolkit().paintBordersFor(compTournament);
		compTournament.setLayout(new GridLayout(1, false));
		
		Section sctnTournament = managedForm.getToolkit().createSection(compTournament, Section.TWISTIE | Section.TITLE_BAR);
		GridData gd_sctnTournament = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_sctnTournament.heightHint = 201;
		gd_sctnTournament.widthHint = 573;
		sctnTournament.setLayoutData(gd_sctnTournament);
		managedForm.getToolkit().paintBordersFor(sctnTournament);
		sctnTournament.setText("Tournament");
		sctnTournament.setExpanded(true);
		
		Composite clientTournament = managedForm.getToolkit().createComposite(sctnTournament, SWT.NONE);
		managedForm.getToolkit().paintBordersFor(clientTournament);
		sctnTournament.setClient(clientTournament);
		clientTournament.setLayout(new GridLayout(2, false));
		
		Label lblName = managedForm.getToolkit().createLabel(clientTournament, "Name", SWT.NONE);
		
		nameText = managedForm.getToolkit().createText(clientTournament, "New Text", SWT.NONE);
		nameText.setText("");
		nameText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblDescription = managedForm.getToolkit().createLabel(clientTournament, "Description", SWT.NONE);
		lblDescription.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		
		descriptionText = managedForm.getToolkit().createText(clientTournament, "New Text", SWT.NONE);
		descriptionText.setText("");
		descriptionText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Composite compCategory = managedForm.getToolkit().createComposite(managedForm.getForm().getBody(), SWT.NONE);
		compCategory.setLayout(new GridLayout(1, false));
		GridData gd_compCategory = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_compCategory.widthHint = 578;
		gd_compCategory.heightHint = 212;
		compCategory.setLayoutData(gd_compCategory);
		managedForm.getToolkit().paintBordersFor(compCategory);
		
		Section sctnCategory = managedForm.getToolkit().createSection(compCategory, Section.TWISTIE | Section.TITLE_BAR);
		GridData gd_sctnCategory = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_sctnCategory.heightHint = 201;
		gd_sctnCategory.widthHint = 569;
		sctnCategory.setLayoutData(gd_sctnCategory);
		managedForm.getToolkit().paintBordersFor(sctnCategory);
		sctnCategory.setText("Categories");
		sctnCategory.setExpanded(true);
		
		Composite clientCategory = managedForm.getToolkit().createComposite(sctnCategory, SWT.NONE);
		managedForm.getToolkit().paintBordersFor(clientCategory);
		sctnCategory.setClient(clientCategory);
	}

	@Override
	public DirtyManager getDirtyManager() {
		return this.dm;
	}
	
	public TournamentDetailsController getController() {
		return mController;
	}

	public Text getNameText() {
		return nameText;
	}

	public void setNameText(Text nameText) {
		this.nameText = nameText;
	}

	public Text getDescriptionText() {
		return descriptionText;
	}

	public void setDescriptionText(Text descriptionText) {
		this.descriptionText = descriptionText;
	}
}
