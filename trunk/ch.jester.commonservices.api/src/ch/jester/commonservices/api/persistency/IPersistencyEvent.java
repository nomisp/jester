package ch.jester.commonservices.api.persistency;

import java.util.Collection;



/**
 * Interface für ein CRUD Event
 *
 */
public interface IPersistencyEvent {
	/**
	 * Die Operation beschreibt was mit dem Event passiert ist.
	 */
	public static enum Operation{
		SAVED, 
		DELETED
	}
	/**Ermittelt auf welcher Entität der Event gefeuert wurde
	 * @return
	 */
	public Class<?> getLoadClass();

	/**Die Operation
	 * @return
	 */
	public Operation getCRUD();

	/**Das Objekt welches den Event gefeurt hat.<br>
	 * Normalerweise ein IDaoService.
	 * @return
	 */
	public Object getSource();

	/**Gibt an, auf welcher Menge die Operation durchgeführt wurde
	 * @return
	 */
	public Collection<?> getLoad();

}