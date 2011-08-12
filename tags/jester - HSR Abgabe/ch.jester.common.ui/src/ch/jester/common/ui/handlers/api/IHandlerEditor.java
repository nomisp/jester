package ch.jester.common.ui.handlers.api;

import ch.jester.commonservices.api.persistency.IEntityObject;
/**
 * Interface um Objekte im Editor zu öffnen<br>
 * Wird von div Handler benutzt.
 *
 * @param <T>
 */
public interface IHandlerEditor<T extends IEntityObject> {

	/**
	 * Weiterreichung um einen Editor zu öffnen.
	 * @param pObject
	 */
	public abstract void handleOpenEditor(Object pObject);

}