package ch.jester.common.ui.handlers.api;

import java.util.Collection;

import ch.jester.commonservices.api.persistency.IEntityObject;

/**
 * Interface um Objekte hinzuzufügen<br>
 * Wird von div Handler benutzt.
 *
 * @param <T>
 */
public interface IHandlerAdd<T extends IEntityObject> {

	/**
	 * Hinzufügen eines Objektes
	 * @param pObject
	 */
	public void handleAdd(T pObject);

	/**
	 * Hinzufügen einer Collection
	 * @param pObjectCollection
	 */
	public void handleAdd(Collection<T> pObjectCollection);

}