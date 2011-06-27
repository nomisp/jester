package ch.jester.system.api.pairing.ui;

import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.editor.FormPage;

import ch.jester.common.settings.ISettingObject;
import ch.jester.common.ui.editorutilities.IDirtyManagerProvider;

public abstract class AbstractSystemSettingsFormPage<T extends ISettingObject> extends FormPage implements IDirtyManagerProvider {

	public AbstractSystemSettingsFormPage(String id, String title) {
		super(id, title);
	}
	
	public AbstractSystemSettingsFormPage(FormEditor editor, String id, String title) {
		super(editor, id, title);
	}
	
	public AbstractSystemSettingsFormPage(ISettingObject settingObject, FormEditor editor, String id, String title) {
		super(editor, id, title);
	}
	
	public abstract ISettingObject getSettingObject();
}
