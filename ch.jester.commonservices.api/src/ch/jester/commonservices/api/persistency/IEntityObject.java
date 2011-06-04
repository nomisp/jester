package ch.jester.commonservices.api.persistency;

import java.io.Serializable;

/**
 * Basisinterface welche Objekte die persistiert werden sollen, implementieren müssen
 *
 */
public interface IEntityObject extends Serializable{
	
	/** Die generierte ID des Objektes
	 * @return
	 */
	public Integer getId();

	/**Setzt die Id
	 * @param id
	 */
	public void setId(Integer id);
	
	/**Sagt aus, ob die Entity bereits einmal gespeichert wurde.
	 * Gibt keine Information über allfällig Dirty States!
	 * @return
	 */
	public boolean isUnsafed();
}
