package ch.jester.ui.tournament.forms;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.BeansObservables;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.IDetailsPage;
import org.eclipse.ui.forms.IFormPart;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.forms.widgets.TableWrapData;
import org.eclipse.ui.forms.widgets.TableWrapLayout;

import ch.jester.common.ui.editorutilities.SWTDirtyManager;
import ch.jester.model.Category;
import org.eclipse.core.databinding.UpdateValueStrategy;

/**
 * Category Details
 * @author Peter
 *
 */
public class CategoryDetailsPage implements IDetailsPage {
	private CategoryMasterDetail masterDetailsBlock;
	private DataBindingContext m_bindingContext;
	private SWTDirtyManager dm = new SWTDirtyManager();
	private IManagedForm mForm;
	private Category category;
	private Text description;
	private boolean dirty = false;


	private ModifyListener textModifyListener = new ModifyListener() {
		
		@Override
		public void modifyText(ModifyEvent e) {
			masterDetailsBlock.setEditorDirty();
			dirty = true;
		}
	};
	
	/**
	 * Konstruktor
	 * @param masterDetailBlock
	 */
	public CategoryDetailsPage(CategoryMasterDetail masterDetailBlock) {
		this.masterDetailsBlock = masterDetailBlock;
	}
	
	@Override
	public void commit(boolean onSave) {
		if (category != null) {
			if (onSave) {
//				category.setDescription(description.getText());
				m_bindingContext.updateModels();
			} else {
				updateUI();
			}
		}
	}

	@Override
	public void dispose() {

	}

	@Override
	public void initialize(IManagedForm pForm) {
		this.mForm = pForm;
	}

	@Override
	public boolean isDirty() {
		return dirty;
	}

	@Override
	public boolean isStale() {
		return false;
	}

	@Override
	public void refresh() {
		removeListeners();
		updateUI();
		addListeners();
	}

	@Override
	public void setFocus() {
		description.setFocus();
	}

	@Override
	public boolean setFormInput(Object input) {
		return false;
	}

	public void updateModel(){
		m_bindingContext.updateModels();
	}
	public void updateUI(){
		m_bindingContext.updateTargets();
	}

	@Override
	public void createContents(Composite parent) {
		TableWrapLayout layout = new TableWrapLayout();
		layout.topMargin = 5;
		layout.leftMargin = 5;
		layout.rightMargin = 2;
		layout.bottomMargin = 2;
		parent.setLayout(layout);

		FormToolkit toolkit = mForm.getToolkit();
		Section s1 = toolkit.createSection(parent, Section.DESCRIPTION|Section.TITLE_BAR);
		s1.marginWidth = 10;
		s1.setText("CategoryDetails.sname");
		s1.setDescription("CategoryDetails.sdescr");
		TableWrapData td = new TableWrapData(TableWrapData.FILL, TableWrapData.TOP);
		td.grabHorizontal = true;
		s1.setLayoutData(td);
		Composite client = toolkit.createComposite(s1);
		GridLayout glayout = new GridLayout();
		glayout.marginWidth = glayout.marginHeight = 0;
		glayout.numColumns = 2;
		client.setLayout(glayout);
		
		GridData gd;
		toolkit.createLabel(client, "Description");
		description = toolkit.createText(client, "", SWT.SINGLE);
		gd = new GridData(GridData.FILL_HORIZONTAL|GridData.VERTICAL_ALIGN_BEGINNING);
		gd.widthHint = 10;
		description.setLayoutData(gd);

		toolkit.paintBordersFor(s1);
		s1.setClient(client);
		
//		dm.add(description);
	}

	@Override
	public void selectionChanged(IFormPart part, ISelection selection) {
		IStructuredSelection ssel = (IStructuredSelection)selection;
		if (ssel.size()==1) {
			category = (Category)ssel.getFirstElement();
			m_bindingContext = initDataBindings();
		}
		else {
			category = null;
		}
		refresh();
	}
	
	private void createSpacer(FormToolkit toolkit, Composite parent, int span) {
		Label spacer = toolkit.createLabel(parent, ""); //$NON-NLS-1$
		GridData gd = new GridData();
		gd.horizontalSpan = span;
		spacer.setLayoutData(gd);
	}
	
	protected DataBindingContext initDataBindings() {
		DataBindingContext bindingContext = new DataBindingContext();
		//
		IObservableValue descriptionObserveTextObserveWidget = SWTObservables.observeText(description, SWT.Modify);
		IObservableValue categoryDescriptionObserveValue = BeansObservables.observeValue(category, "description");
		bindingContext.bindValue(descriptionObserveTextObserveWidget, categoryDescriptionObserveValue, new UpdateValueStrategy(UpdateValueStrategy.POLICY_ON_REQUEST), null);
		//
		return bindingContext;
	}
	
	public SWTDirtyManager getDirtyManager() {
		return dm;
	}
	
	private void addListeners() {
		description.addModifyListener(textModifyListener);
	}

	private void removeListeners() {
		description.removeModifyListener(textModifyListener);
	}
}
