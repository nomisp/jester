package ch.jester.commonservices.api.persistency;

import java.io.Serializable;

/**
 * Basisinterface welche Objekte die persistiert werden sollen, implementieren müssen
 *
 */
public interface IDaoObject extends Serializable{
	
	/** Die generierte ID des Objektes
	 * @return
	 */
	public int getId();

	/**Setzt die Id
	 * @param id
	 */
	public void setId(int id);
}
