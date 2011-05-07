package ch.jester.common.ui.services;

import ch.jester.common.ui.editor.EditorAccessor;
import ch.jester.common.ui.editor.IEditorInputAccess;

public interface IEditorService {
	public EditorAccessor.EditorAccess openEditor(Object pInputObject);
	public EditorAccessor.EditorAccess openEditor(Object pInputObject, String pEditorId);
	public boolean isEditorRegistred(Object pInputObject);
	public void closeEditor(Object pInputObject);
	public void register(Class pObjectClass, Class<? extends IEditorInputAccess<?>> pEditorInputClass, String pEditorId);
}
