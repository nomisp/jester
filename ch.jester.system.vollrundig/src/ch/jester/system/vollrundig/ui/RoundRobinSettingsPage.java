package ch.jester.system.vollrundig.ui;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.TableWrapData;
import org.eclipse.ui.forms.widgets.TableWrapLayout;

import ch.jester.common.settings.ISettingObject;
import ch.jester.common.ui.editorutilities.DirtyManager;
import ch.jester.common.ui.editorutilities.SWTDirtyManager;
import ch.jester.system.api.pairing.StartingNumberGenerationType;
import ch.jester.system.api.pairing.ui.AbstractSystemSettingsFormPage;
import ch.jester.system.vollrundig.RoundRobinSettings;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.core.databinding.beans.PojoObservables;
import org.eclipse.jface.databinding.viewers.ViewersObservables;

/**
 * Form-page für die Einstellungen des Round-Robin Paarungssystemes
 * @author Peter
 *
 */
public class RoundRobinSettingsPage extends AbstractSystemSettingsFormPage<RoundRobinSettings> {
	// TODO Peter: Texte übersetzen!

	private DataBindingContext m_bindingContext;

	private RoundRobinSettings settings;
	private SWTDirtyManager dm = new SWTDirtyManager();
	private Button btnDoublerounded;
	private ComboViewer comboViewer;
	
	public RoundRobinSettingsPage(ISettingObject settings, FormEditor editor, String id, String title) {
		super(editor, id, title);
		this.settings = (RoundRobinSettings)settings;
	}

	/**
	 * Create contents of the form.
	 * @param managedForm
	 */
	@Override
	protected void createFormContent(IManagedForm managedForm) {
		FormToolkit toolkit = managedForm.getToolkit();
		ScrolledForm form = managedForm.getForm();
		form.setText(Messages.RoundRobinSettingsPage_Title);
		Composite body = form.getBody();
		toolkit.decorateFormHeading(form.getForm());
		toolkit.paintBordersFor(body);
		managedForm.getForm().getBody().setLayout(new GridLayout(2, false));
		
		Label lblStartingnumbergeneration = managedForm.getToolkit().createLabel(managedForm.getForm().getBody(), Messages.RoundRobinSettingsPage_StartingNumberGeneration, SWT.NONE);
		lblStartingnumbergeneration.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, false, false, 1, 1));
		
		comboViewer = new ComboViewer(managedForm.getForm().getBody(), SWT.NONE);
		comboViewer.setContentProvider(new ArrayContentProvider());
		comboViewer.setLabelProvider(new LabelProvider() {
			@Override
			public String getText(Object element) {
				StartingNumberGenerationType type = (StartingNumberGenerationType)element;
				if (type == StartingNumberGenerationType.RANDOM) {
					return Messages.RoundRobinSettingsPage_Random;
				} else if (type == StartingNumberGenerationType.ELO) {
					return Messages.RoundRobinSettingsPage_Elo;
				} else if (type == StartingNumberGenerationType.ADDING_ORDER) {
					return Messages.RoundRobinSettingsPage_AddingOrder;
				}
				return type.toString();
			}
		});
		comboViewer.setInput(StartingNumberGenerationType.values());
		comboViewer.setSelection(new StructuredSelection(settings.getStartingNumberGenerationType()));
		
		Combo combo = comboViewer.getCombo();
		GridData gd_combo = new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1);
		gd_combo.widthHint = 329;
		combo.setLayoutData(gd_combo);
		managedForm.getToolkit().paintBordersFor(combo);
				
		btnDoublerounded = new Button(managedForm.getForm().getBody(), SWT.CHECK);
		btnDoublerounded.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1));
		managedForm.getToolkit().adapt(btnDoublerounded, true, true);
		btnDoublerounded.setText(Messages.RoundRobinSettingsPage_DoubleRounded);
		btnDoublerounded.setSelection(settings.getDoubleRounded());
		new Label(managedForm.getForm().getBody(), SWT.NONE);
		m_bindingContext = initDataBindings();
		dm.add(comboViewer.getControl());
		dm.add(btnDoublerounded);
		//dm.add(btnDoublerounded.);
//		dm.add();
	}

	@Override
	public DirtyManager getDirtyManager() {
		return this.dm;
	}
	
	protected DataBindingContext initDataBindings() {
		DataBindingContext bindingContext = new DataBindingContext();
		
		IObservableValue btnDoubleroundedObserveSelectionObserveWidget = SWTObservables.observeSelection(btnDoublerounded);
		IObservableValue settingsDoubleRoundedObserveValue = PojoObservables.observeValue(settings, "doubleRounded"); //$NON-NLS-1$
		bindingContext.bindValue(btnDoubleroundedObserveSelectionObserveWidget, settingsDoubleRoundedObserveValue, null, null);
		
		IObservableValue comboViewerObserveSingleSelection = ViewersObservables.observeSingleSelection(comboViewer);
		IObservableValue settingsStartingNumberGenerationTypeObserveValue = PojoObservables.observeValue(settings, "startingNumberGenerationType"); //$NON-NLS-1$
		bindingContext.bindValue(comboViewerObserveSingleSelection, settingsStartingNumberGenerationTypeObserveValue, null, null);
		
		return bindingContext;
	}

	@Override
	public ISettingObject getSettingObject() {
		return this.settings;
	}
}
