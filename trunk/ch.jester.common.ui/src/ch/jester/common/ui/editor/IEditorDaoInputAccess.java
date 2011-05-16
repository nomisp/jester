package ch.jester.common.ui.editor;

import ch.jester.commonservices.api.persistency.IEntityObject;

public interface IEditorDaoInputAccess<T extends IEntityObject> extends IEditorInputAccess<T>{
	public boolean isAlreadyDirty();
}
