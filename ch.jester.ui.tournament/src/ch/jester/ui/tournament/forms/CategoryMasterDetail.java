package ch.jester.ui.tournament.forms;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
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
import ch.jester.model.Tournament;
import ch.jester.ui.tournament.editors.TournamentEditor;
import ch.jester.ui.tournament.internal.Activator;

public class CategoryMasterDetail extends MasterDetailsBlock {

	private FormPage page;
	private Button btAdd, btRemove;
	private CategoryDetailsPage categoryDetailsPage = new CategoryDetailsPage(this);
	private TableViewer viewer;

	/**
	 * Create the master details block.
	 */
	public CategoryMasterDetail(FormPage page) {
		this.page = page;
	}
	
	/**
	 * @param id
	 * @param title
	 */
	class MasterContentProvider implements IStructuredContentProvider {
		public Object[] getElements(Object inputElement) {
			if (inputElement instanceof IEditorDaoInputAccess) {
				@SuppressWarnings("unchecked")
				IEditorDaoInputAccess<Tournament> input = (IEditorDaoInputAccess<Tournament>) page.getEditor().getEditorInput();
				Tournament tournament = (Tournament)input.getInput();
				return tournament.getCategories().toArray();
			}
			return new Object[0];
		}
		public void dispose() {
		}
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		}
	}
	
	class MasterLabelProvider extends LabelProvider implements ITableLabelProvider {
		public String getColumnText(Object obj, int index) {
			Category cat = (Category) obj;
			return cat.getDescription();
		}
		public Image getColumnImage(Object obj, int index) {
			if (obj instanceof Category) {
				return UIUtility.getImageDescriptor(Activator.getDefault().getActivationContext().getPluginId(),
							"icons/category_16x16.gif").createImage();
			}
//			if (obj instanceof TypeTwo) {
//				return PlatformUI.getWorkbench().getSharedImages().getImage(
//						ISharedImages.IMG_OBJ_FILE);
//			}
			return null;
		}
	}
	protected void createMasterPart(final IManagedForm managedForm, Composite parent) {
		//final ScrolledForm form = managedForm.getForm();
		FormToolkit toolkit = managedForm.getToolkit();
		Section section = toolkit.createSection(parent, Section.DESCRIPTION|Section.TITLE_BAR);
		section.setText("CategoryPropertiesBlock.sname");
		section.setDescription("CategoryPropertiesBlock.sdesc"); 
		section.marginWidth = 10;
		section.marginHeight = 5;
		Composite client = toolkit.createComposite(section, SWT.WRAP);
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		layout.marginWidth = 2;
		layout.marginHeight = 2;
		client.setLayout(layout);
		Table t = toolkit.createTable(client, SWT.NULL);
		GridData gd = new GridData(GridData.FILL_BOTH);
		gd.verticalSpan = 2;
		gd.heightHint = 20;
		gd.widthHint = 100;
		t.setLayoutData(gd);
		toolkit.paintBordersFor(client);
		btAdd = toolkit.createButton(client, "CategoryPropertiesBlock.add", SWT.PUSH);
		gd = new GridData(GridData.VERTICAL_ALIGN_BEGINNING);
		btAdd.setLayoutData(gd);
		section.setClient(client);
		final SectionPart spart = new SectionPart(section);
		managedForm.addPart(spart);
		viewer = new TableViewer(t);
		btRemove = toolkit.createButton(client, "CategoryPropertiesBlock.remove", SWT.PUSH);
		gd = new GridData(GridData.VERTICAL_ALIGN_BEGINNING);
		btRemove.setLayoutData(gd);
		viewer.addSelectionChangedListener(new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent event) {
				managedForm.fireSelectionChanged(spart, event.getSelection());
			}
		});
		viewer.setContentProvider(new MasterContentProvider());
		viewer.setLabelProvider(new MasterLabelProvider());
		viewer.setInput(page.getEditor().getEditorInput());
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
		haction.setToolTipText("CategoryPropertiesBlock.horizontal");
		haction.setImageDescriptor(UIUtility.getImageDescriptor(Activator.getDefault().getActivationContext().getPluginId(),
					"icons/application_tile_horizontal.png"));
		Action vaction = new Action("ver", Action.AS_RADIO_BUTTON) {
			public void run() {
				sashForm.setOrientation(SWT.VERTICAL);
				form.reflow(true);
			}
		};
		vaction.setChecked(false);
		vaction.setToolTipText("CategoryPropertiesBlock.vertical");
		vaction.setImageDescriptor(UIUtility.getImageDescriptor(Activator.getDefault().getActivationContext().getPluginId(),
					"icons/application_tile_vertical.png"));
		form.getToolBarManager().add(haction);
		form.getToolBarManager().add(vaction);
	}
	
	/**
	 * DetailsPages registrieren
	 */
	protected void registerPages(DetailsPart detailsPart) {
		detailsPart.registerPage(Category.class, categoryDetailsPage);
//		detailsPart.registerPage(TypeTwo.class, new TypeTwoDetailsPage());
	}
	
	public void setEditorDirty() {
		((TournamentEditor)page.getEditor()).getDirtyManager().setDirty(true);
	}
	
	public CategoryDetailsPage getCategoryDetailsPage() {
		return categoryDetailsPage;
	}
	
	public void save() {
		categoryDetailsPage.commit(true);
		viewer.setInput(page.getEditor().getEditorInput());
	}
}
