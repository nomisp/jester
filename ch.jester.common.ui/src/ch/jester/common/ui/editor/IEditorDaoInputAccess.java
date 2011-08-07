package ch.jester.common.ui.editor;

import ch.jester.commonservices.api.persistency.IEntityObject;

/**
 * Erweiterung des IEditorInputAccess um Dirty Property.
 * @param <T>
 */
public interface IEditorDaoInputAccess<T extends IEntityObject> extends IEditorInputAccess<T>{
	/**
	 * Ist der Input bereits beim Laden ins UI Dirty?
	 * @return true | false
	 */
	public boolean isAlreadyDirty();
}
