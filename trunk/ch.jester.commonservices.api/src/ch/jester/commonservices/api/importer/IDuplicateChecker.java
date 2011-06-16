package ch.jester.commonservices.api.importer;

import java.util.List;

import javax.persistence.Query;

import ch.jester.commonservices.api.persistency.IDaoService;
import ch.jester.commonservices.api.persistency.IEntityObject;

public interface IDuplicateChecker<T> {
	/**
	 * Sagt aus, ob auf Duplicates geprüft werden soll
	 * @param pDaoService
	 * @return true | false
	 */
	public boolean checkDuplicates(IDaoService<? extends IEntityObject> pDaoService);
	/**Erzeugt eine Query, welche in der Lage ist, Duplicates zu finden
	 * @param pDaoService
	 * @return
	 */
	public Query createDuplicationCheckingQuery(IDaoService<? extends IEntityObject> pDaoService);
	/**
	 * Die Schlüssel, auf welche überprüft werden soll
	 * @param pList
	 * @return
	 */
	public List<?> getDuplicationKeys(List<T> pList);
	/**Den Parameter für die Query
	 * @return
	 */
	public String getParameter();
	/**Wird aufgerufen, wenn Duplicates gefunden wurden.
	 * @param pDaoService
	 * @param pList
	 */
	public void handleDuplicates(IDaoService<? extends IEntityObject> pDaoService, List<T> pList);
}
