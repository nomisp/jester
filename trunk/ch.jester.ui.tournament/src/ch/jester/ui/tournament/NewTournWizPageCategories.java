package ch.jester.ui.tournament;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Table;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.widgets.Button;

/**
 * Wizard Seite um dem Turnier Kategorien hinzuzufügen
 * @author Peter
 *
 */
public class NewTournWizPageCategories extends WizardPage implements SelectionListener {
	private Table categoriesTbl;
	
	private String description;
	private int minElo;
	private int maxElo;
	private int minAge;
	private int maxAge;

	private Button addBtn;

	private Button editBtn;

	/**
	 * Create the wizard.
	 */
	public NewTournWizPageCategories() {
		super("CategoriesPage");
		setTitle("Categories");
		setDescription("Adding categories to the tournament");
		setPageComplete(true);
	}

	/**
	 * Create contents of the wizard.
	 * @param parent
	 */
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);

		setControl(container);
		container.setLayout(new GridLayout(3, false));
		new Label(container, SWT.NONE);
		new Label(container, SWT.NONE);
		new Label(container, SWT.NONE);
		new Label(container, SWT.NONE);
		
		TableViewer categoriesTblViewer = new TableViewer(container, SWT.BORDER | SWT.FULL_SELECTION);
		categoriesTbl = categoriesTblViewer.getTable();
		categoriesTbl.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 3));
		
		TableViewerColumn categoryNameColumn = new TableViewerColumn(categoriesTblViewer, SWT.NONE);
		TableColumn categoryName = categoryNameColumn.getColumn();
		categoryName.setWidth(100);
		categoryName.setText("Description");
		
		TableViewerColumn minEloColumn = new TableViewerColumn(categoriesTblViewer, SWT.NONE);
		TableColumn minElo = minEloColumn.getColumn();
		minElo.setWidth(100);
		minElo.setText("Min. Elo");
		
		addBtn = new Button(container, SWT.NONE);
		GridData gd_addBtn = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_addBtn.widthHint = 54;
		addBtn.setLayoutData(gd_addBtn);
		addBtn.setText("Add");
		addBtn.addSelectionListener(this);
		new Label(container, SWT.NONE);
		
		editBtn = new Button(container, SWT.NONE);
		GridData gd_editBtn = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_editBtn.widthHint = 55;
		editBtn.setLayoutData(gd_editBtn);
		editBtn.setText("Edit");
		editBtn.addSelectionListener(this);
		new Label(container, SWT.NONE);
		
		Button removeBtn = new Button(container, SWT.NONE);
		addBtn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				int selection = categoriesTbl.getSelectionIndex();
			}
		});
		removeBtn.setLayoutData(new GridData(SWT.CENTER, SWT.TOP, false, false, 1, 1));
		removeBtn.setText("Remove");
	}

	@Override
	public void widgetSelected(SelectionEvent e) {
		boolean newDialog = true;
		if (e.getSource() == addBtn) {
			newDialog = true;
		} else if (e.getSource() == editBtn) {
			newDialog = false;
		}
		CategoryDialog dlg = new CategoryDialog(getShell(), newDialog);
		int retVal = dlg.open();
		if (retVal == Dialog.OK) {
			System.out.println("OK pressed");
		}
	}

	@Override
	public void widgetDefaultSelected(SelectionEvent e) {
	}

}
