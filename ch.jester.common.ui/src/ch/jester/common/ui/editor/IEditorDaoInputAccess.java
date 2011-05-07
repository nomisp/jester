package ch.jester.common.ui.editor;

import ch.jester.commonservices.api.persistency.IDaoObject;

public interface IEditorDaoInputAccess<T extends IDaoObject> extends IEditorInputAccess<T>{
	public boolean isAlreadyDirty();
}
