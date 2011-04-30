package ch.jester.dao;

import java.util.Collection;
import java.util.List;


/**
 * Interface für DAO
 *
 * @param <T>
 */
public interface IPersister<T extends IDAO> {
	/**Speicher eine Collection in einer Transaktion
	 * @param pTCollection
	 */
	public void save(Collection<T> pTCollection);
	/**Speichert ein Object
	 * @param pT
	 */
	public void save(T pT);
	/**Löscht ein Objekt
	 * @param pT
	 */
	public void delete(T pT);
	
	public void delete(Collection<T> pTCollection);
	/**
	 * Schliessen und aufräumen
	 */
	public void close();
	
	/** Holt alle Objekte von der DB
	 * @return
	 */
	public List<T> getAll();
	
	/**
	 * Holt alle Objekte anhand einer NamedQuery
	 * So ist es M�glich verschiedene Named Queries zu verwenden (verschiedene Entit�ten)
	 * @param namedQuery
	 * @return Liste mit den Entities
	 */
	public List<T> getAll(String namedQuery);

}
