package ch.jester.common.ui.services;

import ch.jester.common.ui.editor.EditorAccessor;
import ch.jester.common.ui.editor.IEditorInputAccess;

/**
 * Service um Editoren zu registrieren und Objekte in diese zu laden/öffnen.
 *
 */
public interface IEditorService {
	/**Öffnet den Editor mit einem konkreten Objekt
	 * @param pInputObject
	 * @return
	 */
	public EditorAccessor.EditorAccess openEditor(Object pInputObject);
	/**Öffnet einen expliziten Editor mit einem konkreten Objekt
	 * @param pInputObject
	 * @param pEditorId
	 * @return
	 */
	public EditorAccessor.EditorAccess openEditor(Object pInputObject, String pEditorId);
	/**
	 * Untersucht, ob für die Objektklasse eine Editor registriert ist.
	 * {@link IEditorService#register(Class, Class, String)}
	 * @param pInputObject
	 * @return
	 */
	public boolean isEditorRegistred(Object pInputObject);
	/**Schliesst den Editor
	 * @param pInputObject
	 */
	public void closeEditor(Object pInputObject);
	/**Registriert eine Klasse für einen Editor
	 * @param pObjectClass Die Klasse mit welchen der Editor arbeitet
	 * @param pEditorInputClass Die Inputklasse
	 * @param pEditorId Die ID des Editors
	 */
	public void register(Class pObjectClass, Class<? extends IEditorInputAccess<?>> pEditorInputClass, String pEditorId);
}
