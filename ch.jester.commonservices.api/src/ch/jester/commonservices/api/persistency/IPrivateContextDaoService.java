package ch.jester.commonservices.api.persistency;

/**
 * Adaptiert einen DaoService, so dass dieser in einen privaten EntityManager schreibt
 *
 * @param <T>
 */
public interface IPrivateContextDaoService<T extends IEntityObject> extends IDaoService<T> {

	/**
	 * Bei commit() werden der private und der global EntityManager gemerged.
	 */
	public void commit();

	/**
	 * Löschen eines Objekts
	 * @param o
	 */
	public void delete(Object o);
	
	/**
	 * Rollback
	 * @param t
	 */
	public void rollback(T t);
	
}
