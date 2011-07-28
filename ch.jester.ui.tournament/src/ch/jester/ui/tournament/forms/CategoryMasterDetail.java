package ch.jester.ui.tournament.forms;

import messages.Messages;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.forms.DetailsPart;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.MasterDetailsBlock;
import org.eclipse.ui.forms.SectionPart;
import org.eclipse.ui.forms.editor.FormPage;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;

import ch.jester.common.ui.editor.IEditorDaoInputAccess;
import ch.jester.common.ui.utility.UIUtility;
import ch.jester.model.Category;
import ch.jester.model.Round;
import ch.jester.model.Tournament;
import ch.jester.ui.tournament.actions.AddCategoryAction;
import ch.jester.ui.tournament.actions.AddRoundAction;
import ch.jester.ui.tournament.actions.DeleteCategoryAction;
import ch.jester.ui.tournament.actions.DeleteRoundAction;
import ch.jester.ui.tournament.cnf.TournamentLabelProvider;
import ch.jester.ui.tournament.editors.TournamentEditor;
import ch.jester.ui.tournament.internal.Activator;

public class CategoryMasterDetail extends MasterDetailsBlock {

	private FormPage page;
	private Button btAdd, btRemove;
	private CategoryDetailsPage categoryDetailsPage = new CategoryDetailsPage(this);
	private RoundDetailsPage roundDetailsPage = new RoundDetailsPage(this);
	private TreeViewer treeViewer;
	private Tournament tournament;
	private CategoryMasterDetail categoryMDBlock = this;

	/**
	 * Create the master details block.
	 */
	public CategoryMasterDetail(FormPage page) {
		this.page = page;

	}
	
	protected void createMasterPart(final IManagedForm managedForm, Composite parent) {
		FormToolkit toolkit = managedForm.getToolkit();
		Section section = toolkit.createSection(parent, Section.DESCRIPTION|Section.TITLE_BAR);
		section.setText(Messages.CategoryMasterDetail_s_name);
		section.setDescription(Messages.CategoryMasterDetail_s_descr); 
		section.marginWidth = 10;
		section.marginHeight = 5;
		Composite client = toolkit.createComposite(section, SWT.WRAP);
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		layout.marginWidth = 2;
		layout.marginHeight = 2;
		client.setLayout(layout);
		Tree tree = toolkit.createTree(client, SWT.NULL);
		GridData gd = new GridData(GridData.FILL_BOTH);
		gd.verticalSpan = 2;
		gd.heightHint = 20;
		gd.widthHint = 100;
		tree.setLayoutData(gd);
		toolkit.paintBordersFor(client);
		btAdd = toolkit.createButton(client, Messages.CategoryMasterDetail_add, SWT.PUSH);
		gd = new GridData(GridData.VERTICAL_ALIGN_BEGINNING);
		btAdd.setLayoutData(gd);
		btAdd.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Object selection = ((IStructuredSelection)treeViewer.getSelection()).getFirstElement();
				if (selection instanceof Category) {
					new AddCategoryAction(tournament, categoryMDBlock).run();
				} else if (selection instanceof Round) {
					new AddRoundAction(((Round) selection).getCategory(), categoryMDBlock).run();
				}else{
					new AddCategoryAction(tournament, categoryMDBlock).run();
				}
			}
		});
		section.setClient(client);
		final SectionPart spart = new SectionPart(section);
		managedForm.addPart(spart);
		btRemove = toolkit.createButton(client, Messages.CategoryMasterDetail_remove, SWT.PUSH);
		gd = new GridData(GridData.VERTICAL_ALIGN_BEGINNING);
		btRemove.setLayoutData(gd);
		btRemove.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Object selection = ((IStructuredSelection)treeViewer.getSelection()).getFirstElement();
				if (selection instanceof Category) {
					new DeleteCategoryAction((Category)selection, categoryMDBlock).run();
				} else if (selection instanceof Round) {
					new DeleteRoundAction((Round)selection, categoryMDBlock).run();
				}
			}
		});
		
		treeViewer = new TreeViewer(tree);
		treeViewer.addSelectionChangedListener(new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent event) {
				managedForm.fireSelectionChanged(spart, event.getSelection());
				createContextMenu();
			}
		});
		treeViewer.setContentProvider(new TournamentTreeContentProvider());
		treeViewer.setLabelProvider(new TournamentLabelProvider());
		treeViewer.setInput(tournament);
		treeViewer.expandAll();

//		createContextMenu();
	}
	public void setTournament(Tournament t){
		this.tournament=t;
		if(treeViewer!=null){
			treeViewer.setInput(tournament);
			treeViewer.refresh();
		}
	}
	
	/**
	 * Erzeugen des Kontextmenüs für den Tree-Viewer
	 */
	private void createContextMenu() {
		MenuManager menuMgr = new MenuManager();
		Menu menu = menuMgr.createContextMenu(treeViewer.getControl());
		menuMgr.addMenuListener(new IMenuListener() {
			@Override
			public void menuAboutToShow(IMenuManager manager) {
				if (treeViewer.getSelection().isEmpty()) {
					return;
				}

				if (treeViewer.getSelection() instanceof IStructuredSelection) {
					IStructuredSelection selection = (IStructuredSelection) treeViewer.getSelection();
					if (selection.getFirstElement() instanceof Category) {
						Category category = (Category) selection.getFirstElement();
						manager.add(new AddRoundAction(category, categoryMDBlock));
					} else if (selection.getFirstElement() instanceof Round) {
						Round round = (Round) selection.getFirstElement();
						manager.add(new DeleteRoundAction(round, categoryMDBlock));
					}
				}
			}
		});

		menuMgr.setRemoveAllWhenShown(true);
		treeViewer.getControl().setMenu(menu);
	}
	
	/**
	 * Toolbar-Actions für Tile-Horizontal / Tile-Vertical
	 */
	protected void createToolBarActions(IManagedForm managedForm) {
		final ScrolledForm form = managedForm.getForm();
		Action haction = new Action("hor", Action.AS_RADIO_BUTTON) { //$NON-NLS-1$
			public void run() {
				sashForm.setOrientation(SWT.HORIZONTAL);
				form.reflow(true);
			}
		};
		haction.setChecked(true);
		haction.setToolTipText(Messages.CategoryMasterDetail_tt_horizontal);
		haction.setImageDescriptor(UIUtility.getImageDescriptor(Activator.getDefault().getActivationContext().getPluginId(),
					"icons/application_tile_horizontal.png")); //$NON-NLS-1$
		Action vaction = new Action("ver", Action.AS_RADIO_BUTTON) { //$NON-NLS-1$
			public void run() {
				sashForm.setOrientation(SWT.VERTICAL);
				form.reflow(true);
			}
		};
		vaction.setChecked(false);
		vaction.setToolTipText(Messages.CategoryMasterDetail_tt_vertical);
		vaction.setImageDescriptor(UIUtility.getImageDescriptor(Activator.getDefault().getActivationContext().getPluginId(),
					"icons/application_tile_vertical.png")); //$NON-NLS-1$
		form.getToolBarManager().add(haction);
		form.getToolBarManager().add(vaction);
	}
	
	/**
	 * DetailsPages registrieren
	 */
	protected void registerPages(DetailsPart detailsPart) {
		detailsPart.registerPage(Category.class, categoryDetailsPage);
		detailsPart.registerPage(Round.class, roundDetailsPage);
	}
	
	public void setEditorDirty() {
		((TournamentEditor)page.getEditor()).getDirtyManager().setDirty(true);
	}
	
	public CategoryDetailsPage getCategoryDetailsPage() {
		return categoryDetailsPage;
	}
	
	public RoundDetailsPage getRoundDetailsPage() {
		return roundDetailsPage;
	}
	
	public void save() {
		categoryDetailsPage.commit(true);
		roundDetailsPage.commit(true);
		if (treeViewer != null) treeViewer.refresh();
	}
	
	public void refresh() {
		treeViewer.refresh();
	}
}
