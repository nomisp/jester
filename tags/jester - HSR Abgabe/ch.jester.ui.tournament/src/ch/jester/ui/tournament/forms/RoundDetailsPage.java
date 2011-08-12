package ch.jester.ui.tournament.forms;

import java.util.Calendar;
import java.util.Date;

import messages.Messages;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.beans.BeansObservables;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.forms.IDetailsPage;
import org.eclipse.ui.forms.IFormPart;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.forms.widgets.TableWrapData;
import org.eclipse.ui.forms.widgets.TableWrapLayout;

import ch.jester.common.ui.editorutilities.IDirtyListener;
import ch.jester.common.ui.editorutilities.SWTDirtyManager;
import ch.jester.model.Round;

/**
 * Runden Details
 *
 */
public class RoundDetailsPage implements IDetailsPage {
	private CategoryMasterDetail masterDetailsBlock;
	private DataBindingContext m_bindingContext;
	private SWTDirtyManager dm = new SWTDirtyManager();
	private IManagedForm mForm;
	private Round round;
	private DateTime date;
	private boolean dirty = false;
	
	/**
	 * Konstruktor
	 * @param masterDetailBlock
	 */
	public RoundDetailsPage(CategoryMasterDetail masterDetailBlock) {
		this.masterDetailsBlock = masterDetailBlock;
	}
	
	@Override
	public void commit(boolean onSave) {
		if (round != null) {
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
//		removeListeners();
		updateUI();
//		addListeners();
	}

	@Override
	public void setFocus() {
		date.setFocus();
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
		s1.setText(Messages.RoundDetailsPage_s_name);
		s1.setDescription(Messages.RoundDetailsPage_s_descr);
		TableWrapData td = new TableWrapData(TableWrapData.FILL, TableWrapData.TOP);
		td.grabHorizontal = true;
		s1.setLayoutData(td);
		Composite client = toolkit.createComposite(s1);
		client.setLayout(new GridLayout(3, false));
		Label lblDate = toolkit.createLabel(client, Messages.RoundDetailsPage_lbl_date);
		
		date = new DateTime(client, SWT.BORDER);
		toolkit.adapt(date);
		toolkit.paintBordersFor(date);
		
		toolkit.paintBordersFor(s1);
		s1.setClient(client);
		new Label(client, SWT.NONE);
		
		dm.add(date);
		
		dm.addDirtyListener(new IDirtyListener() {
			
			@Override
			public void propertyIsDirty() {
				masterDetailsBlock.setEditorDirty();
				
			}
		});
	}
	
	public void setRound(Round r){
		if(m_bindingContext!=null){
			m_bindingContext.dispose();
		}
		round = r;
	}

	@Override
	public void selectionChanged(IFormPart part, ISelection selection) {
		IStructuredSelection ssel = (IStructuredSelection)selection;
		if (ssel.size()==1) {
			round = (Round)ssel.getFirstElement();
			m_bindingContext = initDataBindings();
		}
		else {
			round = null;
		}
		refresh();
	}
	
	protected DataBindingContext initDataBindings() {
		DataBindingContext bindingContext = new DataBindingContext();
		//
		IObservableValue dateWidget = SWTObservables.observeSelection(date);
		IObservableValue dateValue = BeansObservables.observeValue(round, "date");
		bindingContext.bindValue(dateWidget, dateValue, new UpdateValueStrategy(UpdateValueStrategy.POLICY_UPDATE), null);
		//
		return bindingContext;
	}
	
	public SWTDirtyManager getDirtyManager() {
		return dm;
	}
	
	/**
	 * Gibt das Datum der Runde
	 * 
	 * @return Datum als java.util.Date
	 */
	public Date getDate() {
		Calendar cal = Calendar.getInstance();
		cal.set(date.getYear(), date.getMonth(), date.getDay());
		return cal.getTime();
	}
}
