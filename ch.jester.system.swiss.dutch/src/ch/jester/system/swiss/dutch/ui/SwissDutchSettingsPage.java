package ch.jester.system.swiss.dutch.ui;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.PojoObservables;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.jface.databinding.viewers.ViewersObservables;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;

import ch.jester.common.settings.ISettingObject;
import ch.jester.common.ui.editorutilities.DirtyManager;
import ch.jester.common.ui.editorutilities.SWTDirtyManager;
import ch.jester.system.api.pairing.ui.AbstractSystemSettingsFormPage;
import ch.jester.system.swiss.dutch.SwissDutchSettings;
import ch.jester.system.swiss.dutch.SwissDutchSettings.ByePoint;
import ch.jester.system.swiss.dutch.SwissDutchSettings.RatingType;
import ch.jester.system.swiss.dutch.ui.nl1.Messages;

/**
 * Form-page für die Einstellungen des Schweizer (Dutch) Systems Paarungssystemes
 *
 */
public class SwissDutchSettingsPage extends AbstractSystemSettingsFormPage<SwissDutchSettings> {

	private DataBindingContext m_bindingContext;
	private SwissDutchSettings settings;
	private SWTDirtyManager dm = new SWTDirtyManager();
	private ComboViewer comboViewer;
	private ComboViewer byePointsComboViewer;
	
	public SwissDutchSettingsPage(ISettingObject settings, FormEditor editor, String id, String title) {
		super(editor, id, title);
		this.settings = (SwissDutchSettings)settings;
	}

	/**
	 * Create contents of the form.
	 * @param managedForm
	 */
	@Override
	protected void createFormContent(IManagedForm managedForm) {
		FormToolkit toolkit = managedForm.getToolkit();
		ScrolledForm form = managedForm.getForm();
		form.setText(Messages.SwissDutchSettingsPage_title);
		Composite body = form.getBody();
		toolkit.decorateFormHeading(form.getForm());
		toolkit.paintBordersFor(body);
		managedForm.getForm().getBody().setLayout(new GridLayout(2, false));
		
		Label lblStartingnumbergeneration = managedForm.getToolkit().createLabel(managedForm.getForm().getBody(), Messages.SwissDutchSettingsPage_lbl_ratingType, SWT.NONE);
		GridData gd_lblStartingnumbergeneration = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
		gd_lblStartingnumbergeneration.widthHint = 233;
		lblStartingnumbergeneration.setLayoutData(gd_lblStartingnumbergeneration);
		
		comboViewer = new ComboViewer(managedForm.getForm().getBody(), SWT.READ_ONLY);
		comboViewer.setContentProvider(new ArrayContentProvider());
		comboViewer.setLabelProvider(new LabelProvider() {
			@Override
			public String getText(Object element) {
				RatingType type = (RatingType)element;
				switch (type) {
				case ELO:
					return Messages.SwissDutchSettingsPage_elo;
				case NWZ:
					return Messages.SwissDutchSettingsPage_nationalElo;
				default:
					return type.toString();
				}
			}
		});
		comboViewer.setInput(RatingType.values());
		comboViewer.setSelection(new StructuredSelection(settings.getRatingType()));
		
		Combo combo = comboViewer.getCombo();
		GridData gd_combo = new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1);
		gd_combo.widthHint = 329;
		combo.setLayoutData(gd_combo);
		managedForm.getToolkit().paintBordersFor(combo);
		
		Label lblByepoints = managedForm.getToolkit().createLabel(managedForm.getForm().getBody(), Messages.SwissDutchSettingsPage_lblByepoints_text, SWT.NONE);
		byePointsComboViewer = new ComboViewer(managedForm.getForm().getBody(), SWT.READ_ONLY);
		byePointsComboViewer.setContentProvider(new ArrayContentProvider());
		byePointsComboViewer.setLabelProvider(new LabelProvider() {
			@Override
			public String getText(Object element) {
				ByePoint byePoint = (ByePoint)element;
				switch (byePoint) {
				case ZERO:
					return "0.0";
				case HALF:
					return "0.5";
				case ONE:
					return "1.0";
				default:
					return "1.0";
				}
			}
		});
		byePointsComboViewer.setInput(ByePoint.values());
		byePointsComboViewer.setSelection(new StructuredSelection(settings.getByePoints()));
		Combo byePointsCombo = byePointsComboViewer.getCombo();
		GridData gd_byePointsCombo = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd_byePointsCombo.widthHint = 116;
		byePointsCombo.setLayoutData(gd_byePointsCombo);
		managedForm.getToolkit().paintBordersFor(byePointsCombo);
		m_bindingContext = initDataBindings();
		dm.add(comboViewer.getControl());
	}
	
	protected DataBindingContext initDataBindings() {
		DataBindingContext bindingContext = new DataBindingContext();
		
		IObservableValue comboViewerObserveSingleSelection = ViewersObservables.observeSingleSelection(comboViewer);
		IObservableValue settingsRatingTypeObserveValue = PojoObservables.observeValue(settings, "ratingType"); //$NON-NLS-1$
		bindingContext.bindValue(comboViewerObserveSingleSelection, settingsRatingTypeObserveValue, null, null);
		
		return bindingContext;
	}
	
	@Override
	public DirtyManager getDirtyManager() {
		return this.dm;
	}

	@Override
	public ISettingObject getSettingObject() {
		return this.settings;
	}

}
