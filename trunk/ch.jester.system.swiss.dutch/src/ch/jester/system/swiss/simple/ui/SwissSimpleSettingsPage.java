package ch.jester.system.swiss.simple.ui;

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
import ch.jester.system.swiss.simple.SwissSimpleSettings;
import ch.jester.system.swiss.simple.ui.nl1.Messages;
import ch.jester.system.swiss.simple.util.ByePoint;
import ch.jester.system.swiss.simple.util.FirstRoundColorPref;
import ch.jester.system.swiss.simple.util.RatingType;

/**
 * Form-page für die Einstellungen des Schweizer (Dutch) Systems Paarungssystemes
 *
 */
public class SwissSimpleSettingsPage extends AbstractSystemSettingsFormPage<SwissSimpleSettings> {

	private DataBindingContext m_bindingContext;
	private SwissSimpleSettings settings;
	private SWTDirtyManager dm = new SWTDirtyManager();
	private ComboViewer ratingTypeComboViewer;
	private ComboViewer byePointsComboViewer;
	private ComboViewer firstRoundColorViewer;
	
	public SwissSimpleSettingsPage(ISettingObject settings, FormEditor editor, String id, String title) {
		super(editor, id, title);
		this.settings = (SwissSimpleSettings)settings;
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
		
		ratingTypeComboViewer = new ComboViewer(managedForm.getForm().getBody(), SWT.READ_ONLY);
		ratingTypeComboViewer.setContentProvider(new ArrayContentProvider());
		ratingTypeComboViewer.setLabelProvider(new LabelProvider() {
			@Override
			public String getText(Object element) {
				RatingType type = (RatingType)element;
				switch (type) {
				case ELO:
					return Messages.SwissDutchSettingsPage_elo;
				case NWZ:
					return Messages.SwissDutchSettingsPage_nationalElo;
				case ESTIMATED:
					return Messages.SwissDutchSettingsPage_estimatedElo;
				default:
					return type.toString();
				}
			}
		});
		ratingTypeComboViewer.setInput(RatingType.values());
		ratingTypeComboViewer.setSelection(new StructuredSelection(settings.getRatingType()));
		
		Combo combo = ratingTypeComboViewer.getCombo();
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
					return "0.0"; //$NON-NLS-1$
				case HALF:
					return "0.5"; //$NON-NLS-1$
				case ONE:
					return "1.0"; //$NON-NLS-1$
				default:
					return "1.0"; //$NON-NLS-1$
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
		
		Label lblFirstRoundColor = managedForm.getToolkit().createLabel(managedForm.getForm().getBody(), Messages.SwissDutchSettingsPage_lbl_FirstRoundColor, SWT.NONE);
		firstRoundColorViewer = new ComboViewer(managedForm.getForm().getBody(), SWT.READ_ONLY);
		firstRoundColorViewer.setContentProvider(new ArrayContentProvider());
		firstRoundColorViewer.setLabelProvider(new LabelProvider() {
			@Override
			public String getText(Object element) {
				FirstRoundColorPref colorPref = (FirstRoundColorPref)element;
				switch (colorPref) {
				case WHITE:
					return Messages.SwissDutchSettingsPage_white;
				case BLACK:
					return Messages.SwissDutchSettingsPage_black;
				case RANDOM:
					return Messages.SwissDutchSettingsPage_random;
				default:
					return Messages.SwissDutchSettingsPage_white;
				}
			}
		});
		firstRoundColorViewer.setInput(FirstRoundColorPref.values());
		firstRoundColorViewer.setSelection(new StructuredSelection(settings.getFirstRoundColor()));
		Combo firstRoundColorCombo = firstRoundColorViewer.getCombo();
		GridData gd_firstRoundColorCombo = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd_byePointsCombo.widthHint = 116;
		byePointsCombo.setLayoutData(gd_firstRoundColorCombo);
		managedForm.getToolkit().paintBordersFor(firstRoundColorCombo);
		
		m_bindingContext = initDataBindings();
		dm.add(ratingTypeComboViewer.getControl());
	}
	
	protected DataBindingContext initDataBindings() {
		DataBindingContext bindingContext = new DataBindingContext();
		
		IObservableValue comboViewerObserveSingleSelection = ViewersObservables.observeSingleSelection(ratingTypeComboViewer);
		IObservableValue settingsRatingTypeObserveValue = PojoObservables.observeValue(settings, "ratingType"); //$NON-NLS-1$
		bindingContext.bindValue(comboViewerObserveSingleSelection, settingsRatingTypeObserveValue, null, null);
		IObservableValue byePointsComboViewerrObserveSingleSelection = ViewersObservables.observeSingleSelection(byePointsComboViewer);
		IObservableValue settingsByePointsObserveValue = PojoObservables.observeValue(settings, "byePoints"); //$NON-NLS-1$
		bindingContext.bindValue(byePointsComboViewerrObserveSingleSelection, settingsByePointsObserveValue, null, null);
		IObservableValue firstRoundColorViewerObserveSingleSelection = ViewersObservables.observeSingleSelection(firstRoundColorViewer);
		IObservableValue settingsFirstRoundColorObserveValue = PojoObservables.observeValue(settings, "firstRoundColor"); //$NON-NLS-1$
		bindingContext.bindValue(firstRoundColorViewerObserveSingleSelection, settingsFirstRoundColorObserveValue, null, null);
		
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
