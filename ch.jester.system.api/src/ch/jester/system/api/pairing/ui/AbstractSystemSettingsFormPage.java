package ch.jester.system.api.pairing.ui;

import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.editor.FormPage;

import ch.jester.common.settings.ISettingObject;
import ch.jester.common.ui.editorutilities.IDirtyManagerProvider;

/**
 * Abstrakte Klasse für die Form-Page von
 * Paarungssystem spezifischen Einstellungen
 *
 * @param <T> Ein Setting-Object
 */
public abstract class AbstractSystemSettingsFormPage<T extends ISettingObject> extends FormPage implements IDirtyManagerProvider {

	/**
	 * Konstruktor der eine FormPage erzeugt
	 * @see FormPage
	 * @param id 	Eindeutige Identifikation
	 * @param title	Titel der Page
	 */
	public AbstractSystemSettingsFormPage(String id, String title) {
		super(id, title);
	}
	
	/**
	 * Konstruktor der eine FormPage erzeugt
	 * @see FormPage
	 * @param editor	parent Editor
	 * @param id		Eindeutige Identifikation
	 * @param title		Titel der Page
	 */
	public AbstractSystemSettingsFormPage(FormEditor editor, String id, String title) {
		super(editor, id, title);
	}
	
	/**
	 * Konstruktor der eine FormPage erzeugt
	 * @see FormPage
	 * @param settingObject	Setting-Objekt des Paarungssystems
	 * @param editor		parent Editor
	 * @param id			Eindeutige Identifikation
	 * @param title			Titel der Page
	 */
	public AbstractSystemSettingsFormPage(ISettingObject settingObject, FormEditor editor, String id, String title) {
		super(editor, id, title);
	}
	
	/**
	 * Liefert das zugehörige Setting-Object
	 * @return Setting-Object des Paarungssystems
	 */
	public abstract ISettingObject getSettingObject();
}
