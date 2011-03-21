package ch.jester.dao;

import java.util.Collection;

/**
 * Interface für DAO
 *
 * @param <T>
 */
public interface IPersister<T> {
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
	/**
	 * Schliessen und aufräumen
	 */
	public void close();
}
