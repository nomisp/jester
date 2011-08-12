package ch.jester.ui.tournament.forms;

import messages.Messages;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.UpdateValueStrategy;
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
import ch.jester.common.ui.utility.UIFieldConstraints;
import ch.jester.model.Category;

/**
 * Category Details
 *
 */
public class CategoryDetailsPage implements IDetailsPage {
	private CategoryMasterDetail masterDetailsBlock;
	private DataBindingContext m_bindingContext;
	private SWTDirtyManager dm = new SWTDirtyManager();
	private IManagedForm mForm;
	private Category category;
	private Text description;
	private Text minElo;
	private Text maxElo;
	private Text minAge;
	private Text maxAge;
	private boolean dirty = false;
	private UIFieldConstraints fieldConstraints;
	private ModifyListener textModifyListener = new ModifyListener() {
		
		@Override
		public void modifyText(ModifyEvent e) {
			masterDetailsBlock.setEditorDirty();
			dirty = true;
			//m_bindingContext.updateModels();
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
				//m_bindingContext.updateModels();
			} else {
				//updateUI();
			}
		}
	}

	@Override
	public void dispose() {
		if(m_bindingContext!=null){
			m_bindingContext.dispose();
		}
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

	@SuppressWarnings("unused")
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
		s1.setText(Messages.CategoryDetailsPage_s_name);
		s1.setDescription(Messages.CategoryDetailsPage_s_descr);
		TableWrapData td = new TableWrapData(TableWrapData.FILL, TableWrapData.TOP);
		td.grabHorizontal = true;
		s1.setLayoutData(td);
		Composite client = toolkit.createComposite(s1);
		client.setLayout(new GridLayout(2, false));
		Label lblDescription = toolkit.createLabel(client, Messages.CategoryDetailsPage_description);
		description = toolkit.createText(client, "", SWT.BORDER); //$NON-NLS-1$
		description.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblMinElo = toolkit.createLabel(client, Messages.CategoryDetailsPage_minElo, SWT.NONE);
		minElo = toolkit.createText(client, "", SWT.BORDER); //$NON-NLS-1$
		minElo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblMaxElo = toolkit.createLabel(client, Messages.CategoryDetailsPage_maxElo, SWT.NONE);
		maxElo = toolkit.createText(client, "", SWT.BORDER); //$NON-NLS-1$
		maxElo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblMinAge = toolkit.createLabel(client, Messages.CategoryDetailsPage_minAge, SWT.NONE);
		minAge = toolkit.createText(client, "", SWT.BORDER); //$NON-NLS-1$
		minAge.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblMaxAge = toolkit.createLabel(client, Messages.CategoryDetailsPage_maxAge, SWT.NONE);
		maxAge = toolkit.createText(client, "", SWT.BORDER); //$NON-NLS-1$
		maxAge.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		toolkit.paintBordersFor(s1);
		s1.setClient(client);
		
		fieldConstraints = new UIFieldConstraints(Category.class);
		fieldConstraints.addConstraint(description, "description");
		fieldConstraints.addConstraint(minElo, "minElo");
		fieldConstraints.addConstraint(maxElo, "maxElo");
		fieldConstraints.addConstraint(maxAge, "maxAge");
		fieldConstraints.addConstraint(minAge, "minAge");
		
//		dm.add(description);
	}
	
	public void setCategory(Category cat){
		if(m_bindingContext!=null){
			m_bindingContext.dispose();
		}
		category = cat;
	}

	public boolean isValid(){
		if(fieldConstraints!=null){
			return !fieldConstraints.hasErrors();
		}
		return true;
	}
	@Override
	public void selectionChanged(IFormPart part, ISelection selection) {
		IStructuredSelection ssel = (IStructuredSelection)selection;
		if (ssel.size()==1) {
			category = (Category)ssel.getFirstElement();
			removeListeners();
			m_bindingContext = initDataBindings();
			addListeners();
		}
		else {
			category = null;
		}
		refresh();
	}
	
	/**
	 * Initialieren des Databindings
	 * @return DataBindingContext
	 */
	protected DataBindingContext initDataBindings() {
		DataBindingContext bindingContext = new DataBindingContext();
		//
		IObservableValue descriptionObserveTextObserveWidget = SWTObservables.observeText(description, SWT.Modify);
		IObservableValue categoryDescriptionObserveValue = BeansObservables.observeValue(category, "description"); //$NON-NLS-1$
		bindingContext.bindValue(descriptionObserveTextObserveWidget, categoryDescriptionObserveValue, new UpdateValueStrategy(UpdateValueStrategy.POLICY_UPDATE), null);
		IObservableValue minEloObserveTextObserveWidget = SWTObservables.observeText(minElo, SWT.Modify);
		IObservableValue categoryMinEloObserveValue = BeansObservables.observeValue(category, "minElo"); //$NON-NLS-1$
		bindingContext.bindValue(minEloObserveTextObserveWidget, categoryMinEloObserveValue, new UpdateValueStrategy(UpdateValueStrategy.POLICY_UPDATE), null);
		IObservableValue maxEloObserveTextObserveWidget = SWTObservables.observeText(maxElo, SWT.Modify);
		IObservableValue categoryMaxEloObserveValue = BeansObservables.observeValue(category, "maxElo"); //$NON-NLS-1$
		bindingContext.bindValue(maxEloObserveTextObserveWidget, categoryMaxEloObserveValue, new UpdateValueStrategy(UpdateValueStrategy.POLICY_UPDATE), null);
		IObservableValue minAgeObserveTextObserveWidget = SWTObservables.observeText(minAge, SWT.Modify);
		IObservableValue categoryMinAgeObserveValue = BeansObservables.observeValue(category, "minAge"); //$NON-NLS-1$
		bindingContext.bindValue(minAgeObserveTextObserveWidget, categoryMinAgeObserveValue, new UpdateValueStrategy(UpdateValueStrategy.POLICY_UPDATE), null);
		IObservableValue maxAgeObserveTextObserveWidget = SWTObservables.observeText(maxAge, SWT.Modify);
		IObservableValue categoryMaxAgeObserveValue = BeansObservables.observeValue(category, "maxAge"); //$NON-NLS-1$
		bindingContext.bindValue(maxAgeObserveTextObserveWidget, categoryMaxAgeObserveValue, new UpdateValueStrategy(UpdateValueStrategy.POLICY_UPDATE), null);
		//
		return bindingContext;
	}
	
	public SWTDirtyManager getDirtyManager() {
		return dm;
	}
	
	/**
	 * Listener anmelden
	 */
	private void addListeners() {
		description.addModifyListener(textModifyListener);
		minElo.addModifyListener(textModifyListener);
		maxElo.addModifyListener(textModifyListener);
		minAge.addModifyListener(textModifyListener);
		maxAge.addModifyListener(textModifyListener);
	}

	/**
	 * Listener entfernen
	 */
	private void removeListeners() {
		description.removeModifyListener(textModifyListener);
		minElo.removeModifyListener(textModifyListener);
		maxElo.removeModifyListener(textModifyListener);
		minAge.removeModifyListener(textModifyListener);
		maxAge.removeModifyListener(textModifyListener);
	}
}
