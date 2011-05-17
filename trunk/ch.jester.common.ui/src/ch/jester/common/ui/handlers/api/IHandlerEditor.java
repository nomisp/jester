package ch.jester.common.ui.handlers.api;

import ch.jester.commonservices.api.persistency.IEntityObject;

public interface IHandlerEditor<T extends IEntityObject> {

	public abstract void handleOpenEditor(Object pObject);

}