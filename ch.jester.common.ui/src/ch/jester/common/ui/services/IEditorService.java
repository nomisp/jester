package ch.jester.common.ui.services;

import ch.jester.common.ui.utility.IEditorInputAccess;

public interface IEditorService {
	public void openEditor(Object pInputObject);
	public void openEditor(Object pInputObject, String pEditorId);
	public void closeEditor(Object pInputObject);
	public void register(Class<?> pObjectClass, Class<? extends IEditorInputAccess<?>> pEditorInputClass, String pEditorId);
}
