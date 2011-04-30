package ch.jester.dao;

import java.util.Collection;
import java.util.List;


/**
 * Interface fÃ¼r DAO
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
	/**LÃ¶scht ein Objekt
	 * @param pT
	 */
	public void delete(T pT);
	
	public void delete(Collection<T> pTCollection);
	/**
	 * Schliessen und aufrÃ¤umen
	 */
	public void close();
	
	/** Holt alle Objekte von der DB
	 * @return
	 */
	public List<T> getAll();
	
	/**
	 * Holt alle Objekte anhand einer NamedQuery
	 * So ist es Möglich verschiedene Named Queries zu verwenden (verschiedene Entitäten)
	 * @param namedQuery
	 * @return Liste mit den Entities
	 */
	public List<T> getAll(String namedQuery);

}
