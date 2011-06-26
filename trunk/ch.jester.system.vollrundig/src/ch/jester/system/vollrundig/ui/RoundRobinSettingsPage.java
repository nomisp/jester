package ch.jester.system.vollrundig.ui;

import org.eclipse.jface.viewers.ComboViewer;
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
import ch.jester.system.api.pairing.ui.AbstractSystemSettingsFormPage;
import ch.jester.system.vollrundig.RoundRobinSettings;

/**
 * Form-page für die Einstellungen des Round-Robin Paarungssystemes
 * @author Peter
 *
 */
public class RoundRobinSettingsPage extends AbstractSystemSettingsFormPage<RoundRobinSettings> {

//	private RoundRobinSettings settings;
	private SWTDirtyManager dm = new SWTDirtyManager();
	private Button btnDoublerounded;
	
	public RoundRobinSettingsPage(FormEditor editor, String id, String title) {
		super(editor, id, title);
//		this.settings = settings;
	}

	/**
	 * Create contents of the form.
	 * @param managedForm
	 */
	@Override
	protected void createFormContent(IManagedForm managedForm) {
		FormToolkit toolkit = managedForm.getToolkit();
		ScrolledForm form = managedForm.getForm();
		form.setText("RoundRobinSettingsPage");
		Composite body = form.getBody();
		toolkit.decorateFormHeading(form.getForm());
		toolkit.paintBordersFor(body);
		{
			TableWrapLayout tableWrapLayout = new TableWrapLayout();
			tableWrapLayout.numColumns = 2;
			managedForm.getForm().getBody().setLayout(tableWrapLayout);
		}
		
		Composite compSettings = managedForm.getToolkit().createComposite(managedForm.getForm().getBody(), SWT.NONE);
		managedForm.getToolkit().paintBordersFor(compSettings);
		{
			compSettings.setLayout(new TableWrapLayout());
		}
		
		btnDoublerounded = new Button(managedForm.getForm().getBody(), SWT.CHECK);
		managedForm.getToolkit().adapt(btnDoublerounded, true, true);
		btnDoublerounded.setText("DoubleRounded");
//		btnDoublerounded.setSelection(settings.getDoubleRounded());
		
		Label lblStartingnumbergeneration = managedForm.getToolkit().createLabel(managedForm.getForm().getBody(), "StartingNumberGeneration", SWT.NONE);
		lblStartingnumbergeneration.setLayoutData(new TableWrapData(TableWrapData.RIGHT, TableWrapData.TOP, 1, 1));
		
		ComboViewer comboViewer = new ComboViewer(managedForm.getForm().getBody(), SWT.NONE);
		Combo combo = comboViewer.getCombo();
		combo.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB, TableWrapData.TOP, 1, 1));
		managedForm.getToolkit().paintBordersFor(combo);
	}

	@Override
	public DirtyManager getDirtyManager() {
		return this.dm;
	}

}
