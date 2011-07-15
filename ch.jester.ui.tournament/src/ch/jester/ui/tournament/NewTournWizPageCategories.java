package ch.jester.ui.tournament;

import java.util.ArrayList;
import java.util.List;

import messages.Messages;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

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
	private Button addBtn;
	private Button removeBtn;
	private TableViewer categoriesTblViewer;

	/**
	 * Create the wizard.
	 */
	public NewTournWizPageCategories() {
		super("CategoriesPage"); //$NON-NLS-1$
		setTitle(Messages.NewTournWizPageCategories_title);
		setDescription(Messages.NewTournWizPageCategories_description);
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
		categoryName.setText(Messages.NewTournWizPageCategories_tableCol_description);
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
		minEloCol.setText(Messages.NewTournWizPageCategories_tableCol_minElo);
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
		maxEloCol.setText(Messages.NewTournWizPageCategories_tableCol_maxElo);
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
		minAgeCol.setText(Messages.NewTournWizPageCategories_tableCol_minAge);
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
		maxAgeCol.setText(Messages.NewTournWizPageCategories_tableCol_maxAge);
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
		roundsCol.setWidth(118);
		roundsCol.setText(Messages.NewTournWizPageCategories_tableCol_rounds);
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
		addBtn.setText(Messages.NewTournWizPageCategories_btn_add);
		addBtn.addSelectionListener(this);
		new Label(container, SWT.NONE);
		
		removeBtn = new Button(container, SWT.NONE);
		removeBtn.addSelectionListener(this);
		removeBtn.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1));
		removeBtn.setText(Messages.NewTournWizPageCategories_btn_remove);
		new Label(container, SWT.NONE);
		new Label(container, SWT.NONE);
	}

	@Override
	public void widgetSelected(SelectionEvent e) {
		Category cat = null;
		if (e.getSource() == addBtn) {
			cat = ModelFactory.getInstance().createCategory(Messages.NewTournWizPageCategories_entity_Category_name + (categories.size()+1));
			categories.add(cat);
			categoriesTblViewer.add(cat);
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

	public List<Category> getCategories() {
		return categories;
	}
	
}
