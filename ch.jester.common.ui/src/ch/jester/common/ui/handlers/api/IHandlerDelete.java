package ch.jester.common.ui.handlers.api;

import java.util.List;

import ch.jester.commonservices.api.persistency.IEntityObject;
import ch.jester.commonservices.exceptions.ProcessingException;

/**
 * Interface um Objekte zu löschen<br>
 * Wird von div. Handler benutzt.
 *
 * @param <T>
 */
public interface IHandlerDelete<T extends IEntityObject> {
	/**
	 * Löschen eines Objektes
	 * Der Aufruf kann von einem nicht UI-Thread erfolgen!
	 * @param pObject
	 */
	//public abstract void handleDelete(T pObject);

	/**
	 * Löschen einer Collection
	 * Der Aufruf kann von einem nicht UI-Thread erfolgen!
	 * @param pObjectCollection
	 */
	public abstract void handleDelete(final List<T> pList) throws ProcessingException;

}