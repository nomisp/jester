package ch.jester.ui.tournament.forms;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.editor.FormPage;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.custom.ScrolledComposite;

public class WirePlayerForm extends FormPage {
	private Text searchField;
	private Table searchResultTable;
	private Button btnSearch;
	private Table addedPlayersTable;

	/**
	 * Create the form page.
	 * @param id
	 * @param title
	 */
	public WirePlayerForm(String id, String title) {
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
	public WirePlayerForm(FormEditor editor, String id, String title) {
		super(editor, id, title);
		setPartName("Add Player");
	}

	/**
	 * Create contents of the form.
	 * @param managedForm
	 */
	@Override
	protected void createFormContent(IManagedForm managedForm) {
		FormToolkit toolkit = managedForm.getToolkit();
		ScrolledForm form = managedForm.getForm();
		form.setText("Add Player to Category");
		Composite body = form.getBody();
		toolkit.decorateFormHeading(form.getForm());
		toolkit.paintBordersFor(body);
		
		Section sctnSearchPlayer = managedForm.getToolkit().createSection(managedForm.getForm().getBody(), Section.TWISTIE | Section.TITLE_BAR);
		sctnSearchPlayer.setBounds(10, 10, 574, 164);
		managedForm.getToolkit().paintBordersFor(sctnSearchPlayer);
		sctnSearchPlayer.setText("Search Player");
		sctnSearchPlayer.setExpanded(true);
		
		Composite composite = managedForm.getToolkit().createComposite(sctnSearchPlayer, SWT.NONE);
		managedForm.getToolkit().paintBordersFor(composite);
		sctnSearchPlayer.setClient(composite);
		composite.setLayout(new GridLayout(3, false));
		new Label(composite, SWT.NONE);
		
		searchField = managedForm.getToolkit().createText(composite, "New Text", SWT.NONE);
		searchField.setText("");
		searchField.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		btnSearch = managedForm.getToolkit().createButton(composite, "Search", SWT.NONE);
		
		Label label = managedForm.getToolkit().createSeparator(composite, SWT.HORIZONTAL);
		GridData gd_label = new GridData(SWT.LEFT, SWT.CENTER, false, false, 3, 1);
		gd_label.widthHint = 551;
		label.setLayoutData(gd_label);
		new Label(composite, SWT.NONE);
		
		ScrolledComposite scrolledComposite = new ScrolledComposite(composite, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		scrolledComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
		managedForm.getToolkit().adapt(scrolledComposite);
		managedForm.getToolkit().paintBordersFor(scrolledComposite);
		scrolledComposite.setExpandHorizontal(true);
		scrolledComposite.setExpandVertical(true);
		
		searchResultTable = managedForm.getToolkit().createTable(scrolledComposite, SWT.MULTI);
		managedForm.getToolkit().paintBordersFor(searchResultTable);
		searchResultTable.setHeaderVisible(true);
		searchResultTable.setLinesVisible(true);
		scrolledComposite.setContent(searchResultTable);
		scrolledComposite.setMinSize(searchResultTable.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		new Label(composite, SWT.NONE);
		
		Section sctnAddedPlayers = managedForm.getToolkit().createSection(managedForm.getForm().getBody(), Section.TWISTIE | Section.TITLE_BAR);
		sctnAddedPlayers.setBounds(10, 206, 574, 224);
		managedForm.getToolkit().paintBordersFor(sctnAddedPlayers);
		sctnAddedPlayers.setText("Added Players");
		sctnAddedPlayers.setExpanded(true);
		
		Composite composite_1 = managedForm.getToolkit().createComposite(sctnAddedPlayers, SWT.NONE);
		managedForm.getToolkit().paintBordersFor(composite_1);
		sctnAddedPlayers.setClient(composite_1);
		
		addedPlayersTable = managedForm.getToolkit().createTable(composite_1, SWT.NONE);
		addedPlayersTable.setBounds(10, 10, 542, 180);
		managedForm.getToolkit().paintBordersFor(addedPlayersTable);
		addedPlayersTable.setHeaderVisible(true);
		addedPlayersTable.setLinesVisible(true);
	}
}
