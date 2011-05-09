package ch.jester.ui.tournament;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Table;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.widgets.Button;

import ch.jester.model.Category;
import ch.jester.model.factories.ModelFactory;

/**
 * Wizard Seite um dem Turnier Kategorien hinzuzufügen
 * @author Peter
 *
 */
public class NewTournWizPageCategories extends WizardPage implements SelectionListener {
	private Table categoriesTbl;
	
	private List<Category> categories;
	
	private String description;
	private int minElo;
	private int maxElo;
	private int minAge;
	private int maxAge;
	private int rounds;

	private Button addBtn;

	private Button editBtn;

	private Button removeBtn;

	private TableViewer categoriesTblViewer;

	/**
	 * Create the wizard.
	 */
	public NewTournWizPageCategories() {
		super("CategoriesPage");
		setTitle("Categories");
		setDescription("Adding categories to the tournament");
		setPageComplete(true);
		categories = new ArrayList<Category>();
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
		
		categoriesTblViewer = new TableViewer(container, SWT.BORDER | SWT.FULL_SELECTION);
		categoriesTbl = categoriesTblViewer.getTable();
		categoriesTbl.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 3));
		categoriesTbl.setHeaderVisible(true);
		categoriesTbl.setLinesVisible(true);
		categoriesTblViewer.setContentProvider(ArrayContentProvider.getInstance());
		
		TableViewerColumn categoryNameColumn = new TableViewerColumn(categoriesTblViewer, SWT.NONE);
		TableColumn categoryName = categoryNameColumn.getColumn();
		categoryName.setWidth(100);
		categoryName.setText("Description");
		categoryNameColumn.setLabelProvider(new CellLabelProvider() {
		    public void update(ViewerCell cell) {
		        cell.setText(((Category) cell.getElement()).getDescription());
		    }
		});
		categoryNameColumn.setEditingSupport(new EditingSupport(categoriesTblViewer) {

		    protected boolean canEdit(Object element) {
		        return true;
		    }

		    protected CellEditor getCellEditor(Object element) {
		        return new TextCellEditor(categoriesTbl);
		    }

		    protected Object getValue(Object element) {
		        return ((Category) element).getDescription();
		    }

		    protected void setValue(Object element, Object value) {
		        ((Category) element).setDescription(String.valueOf(value));
		        categoriesTblViewer.refresh(element);
		    }

		});
		
		TableViewerColumn minEloColumn = new TableViewerColumn(categoriesTblViewer, SWT.NONE);
		TableColumn minEloCol = minEloColumn.getColumn();
		minEloCol.setWidth(80);
		minEloCol.setText("Min. Elo");
		minEloColumn.setLabelProvider(new CellLabelProvider() {
		    public void update(ViewerCell cell) {
		    	Integer minElo = ((Category) cell.getElement()).getMinElo() != null ? ((Category) cell.getElement()).getMinElo() : 0;
		        cell.setText((Integer.toString(minElo)));
		    }
		});
		minEloColumn.setEditingSupport(new EditingSupport(categoriesTblViewer) {

		    protected boolean canEdit(Object element) {
		        return true;
		    }

		    protected CellEditor getCellEditor(Object element) {
		        return new TextCellEditor(categoriesTbl);
		    }

		    protected Object getValue(Object element) {
		        return ((Category) element).getMinElo().toString();
		    }

		    protected void setValue(Object element, Object value) {
		        ((Category) element).setMinElo(Integer.parseInt(String.valueOf(value)));
		        categoriesTblViewer.refresh(element);
		    }

		});

		TableViewerColumn maxEloColumn = new TableViewerColumn(categoriesTblViewer, SWT.NONE);
		TableColumn maxEloCol = maxEloColumn.getColumn();
		maxEloCol.setWidth(80);
		maxEloCol.setText("Max. Elo");
		maxEloColumn.setLabelProvider(new CellLabelProvider() {
		    public void update(ViewerCell cell) {
		    	Integer maxElo = ((Category) cell.getElement()).getMaxElo() != null ? ((Category) cell.getElement()).getMaxElo() : 0;
		        cell.setText((Integer.toString(maxElo)));
		    }
		});
		maxEloColumn.setEditingSupport(new EditingSupport(categoriesTblViewer) {

		    protected boolean canEdit(Object element) {
		        return true;
		    }

		    protected CellEditor getCellEditor(Object element) {
		        return new TextCellEditor(categoriesTbl);
		    }

		    protected Object getValue(Object element) {
		        return ((Category) element).getMaxElo().toString();
		    }

		    protected void setValue(Object element, Object value) {
		        ((Category) element).setMaxElo(Integer.parseInt(String.valueOf(value)));
		        categoriesTblViewer.refresh(element);
		    }

		});
		
		TableViewerColumn minAgeColumn = new TableViewerColumn(categoriesTblViewer, SWT.NONE);
		TableColumn minAgeCol = minAgeColumn.getColumn();
		minAgeCol.setWidth(80);
		minAgeCol.setText("Min. Age");
		minAgeColumn.setLabelProvider(new CellLabelProvider() {
		    public void update(ViewerCell cell) {
		    	Integer minAge = ((Category) cell.getElement()).getMinAge() != null ? ((Category) cell.getElement()).getMinAge() : 0;
		        cell.setText((Integer.toString(minAge)));
		    }
		});
		minAgeColumn.setEditingSupport(new EditingSupport(categoriesTblViewer) {

		    protected boolean canEdit(Object element) {
		        return true;
		    }

		    protected CellEditor getCellEditor(Object element) {
		        return new TextCellEditor(categoriesTbl);
		    }

		    protected Object getValue(Object element) {
		        return ((Category) element).getMinAge().toString();
		    }

		    protected void setValue(Object element, Object value) {
		        ((Category) element).setMinAge(Integer.parseInt(String.valueOf(value)));
		        categoriesTblViewer.refresh(element);
		    }

		});

		TableViewerColumn maxAgeColumn = new TableViewerColumn(categoriesTblViewer, SWT.NONE);
		TableColumn maxAgeCol = maxAgeColumn.getColumn();
		maxAgeCol.setWidth(80);
		maxAgeCol.setText("Max. Age");
		maxAgeColumn.setLabelProvider(new CellLabelProvider() {
		    public void update(ViewerCell cell) {
		    	Integer maxAge = ((Category) cell.getElement()).getMaxAge() != null ? ((Category) cell.getElement()).getMaxAge() : 0;
		        cell.setText((Integer.toString(maxAge)));
		    }
		});
		maxAgeColumn.setEditingSupport(new EditingSupport(categoriesTblViewer) {

		    protected boolean canEdit(Object element) {
		        return true;
		    }

		    protected CellEditor getCellEditor(Object element) {
		        return new TextCellEditor(categoriesTbl);
		    }

		    protected Object getValue(Object element) {
		        return ((Category) element).getMaxAge().toString();
		    }

		    protected void setValue(Object element, Object value) {
		        ((Category) element).setMaxAge(Integer.parseInt(String.valueOf(value)));
		        categoriesTblViewer.refresh(element);
		    }

		});

		TableViewerColumn roundsColumn = new TableViewerColumn(categoriesTblViewer, SWT.NONE);
		TableColumn roundsCol = roundsColumn.getColumn();
		roundsCol.setWidth(80);
		roundsCol.setText("Rounds");
		roundsColumn.setLabelProvider(new CellLabelProvider() {
		    public void update(ViewerCell cell) {
		    	Integer maxRounds = ((Category) cell.getElement()).getMaxRounds() != null ? ((Category) cell.getElement()).getMaxRounds() : 0;
		        cell.setText((Integer.toString(maxRounds)));
		    }
		});
		roundsColumn.setEditingSupport(new EditingSupport(categoriesTblViewer) {

		    protected boolean canEdit(Object element) {
		        return true;
		    }

		    protected CellEditor getCellEditor(Object element) {
		        return new TextCellEditor(categoriesTbl);
		    }

		    protected Object getValue(Object element) {
		        return ((Category) element).getMaxRounds().toString();
		    }

		    protected void setValue(Object element, Object value) {
		        ((Category) element).setMaxRounds(Integer.parseInt(String.valueOf(value)));
		        categoriesTblViewer.refresh(element);
		    }

		});

		categoriesTblViewer.setInput(categories);
		
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
		
		removeBtn = new Button(container, SWT.NONE);
		removeBtn.addSelectionListener(this);
		removeBtn.setLayoutData(new GridData(SWT.CENTER, SWT.TOP, false, false, 1, 1));
		removeBtn.setText("Remove");
	}

	@Override
	public void widgetSelected(SelectionEvent e) {
		boolean newDialog = true;
		Category cat = null;
		if (e.getSource() == addBtn) {
			newDialog = true;
			cat = ModelFactory.getInstance().createCategory("Category " + (categories.size()+1));
			categories.add(cat);
			categoriesTblViewer.add(cat);
			
//			CategoryDialog dlg = new CategoryDialog(getShell(), newDialog, cat);
//			int retVal = dlg.open();
//			if (retVal == Dialog.OK) {
//				System.out.println("OK pressed: adding category");
//				categories.add(dlg.getCategory());
//			}
		} else if (e.getSource() == editBtn) {
			newDialog = false;
			cat = categories.get(categoriesTbl.getSelectionIndex());
//			CategoryDialog dlg = new CategoryDialog(getShell(), newDialog, cat);
//			int retVal = dlg.open();
//			if (retVal == Dialog.OK) {
//				System.out.println("OK pressed");
//				
//			}
		} else if (e.getSource() == removeBtn) {
			// TODO peter: Sicherheitsabfrage
			int selection = categoriesTbl.getSelectionIndex();
			categoriesTblViewer.remove(categories.get(selection));
			categories.remove(selection);
		}
	}

	@Override
	public void widgetDefaultSelected(SelectionEvent e) {
	}

}
