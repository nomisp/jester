package ch.jester.commonservices.api.persistency;

import java.util.Collection;
import java.util.List;

import javax.persistence.Query;

import ch.jester.commonservices.exceptions.ProcessingException;

/**
 * Service welcher die möglichkeit bietet, auf eine Entität zuzugreifen.
 * Es wird ein IPersistencyEvent an die IPersistencyEventQueue gefeuert wenn Objekte gespeichert oder gelöscht werden.
 *
 * @param <T>
 */
public interface IDaoService<T extends IEntityObject> {
	
	/**Den Type
	 * @return
	 */
	public Class<T> getDaoClass();
	
	/**
	 * Speichert eine Collection in einer Transaktion.
	 * Sämtliche Objekte werden detached;
	 * @param pTCollection
	 */
	public void saveBatch(Collection<T> pTCollection);
	
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
	
	/**Löscht die übergebenen Objekte in der Collection
	 * @param pTCollection
	 */
	public void delete(Collection<T> pTCollection);
	/**
	 * Schliessen und aufräumen
	 */
	public void close();
	
	/** Holt alle Objekte von der DB
	 * @return
	 */
	public List<T> getAll();
	
	/**Wie viele Objekte auf der DB gibt es?
	 * @return
	 */
	public int count();
	
	/**Holt nur die Objekte im angegebenen Range
	 * @param from
	 * @param to
	 * @return
	 */
	public List<T> getFromTo(int from, int to);
	
	/**
	 * Führt eine Parameterlose NamedQuery aus z.B. um alle Entitäten zu holen.
	 * So ist es Möglich verschiedene Named Queries zu verwenden (verschiedene Entitäten)
	 * @param namedQuery
	 * @return Liste mit den Entities
	 */
	public List<T> executeNamedQuery(String namedQuery);

	public List<T> executeNamedQuery(String queryName, String pPara, Object pVal);
	
	public Query createQuery(String query);
	
	public Query createNamedQuery(String query);
	
	public  T find(Integer id);
	
	public IQueueNotifier getNotifier();
	
	public IPrivateContextDaoService<T> privateContext() throws ProcessingException;
	
}
