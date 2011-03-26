package ch.jester.orm;

import java.sql.Connection;

import javax.persistence.EntityManagerFactory;

public interface IORMConfiguration {

	/**
	 * liefert das Passwort	
	 */
	public abstract String getPassword();

	/**
	 * liefert den User
	 * @return
	 */
	public abstract String getUser();

	/**
	 * liefert die Verbindungs-URL
	 * @return
	 */
	public abstract String getConnectionurl();

	public abstract String getLocalConnection();

	/**
	 * liefert die Ip des Datenbankservers
	 * @return
	 */
	public abstract String getDefaultPath();

	/**
	 * liefert den Namen der Datenbank
	 * @return
	 */
	public abstract String getDbname();

	/**
	 * liefert den Namen der Class des JDBC-Treibers
	 * liest diesen aus der Extension des Plugins aus
	 * @return
	 */
	public abstract String getConnectiondriverclass();

	/**
	 * liefert den Namen der Class des SQL-dialektes
	 * liest diesen aus der Extension des Plugins aus
	 * @return
	 */
	public abstract String getSqldialect();

	/**
	 * liefert den Namen des Subprotocols 
	 * dieser wird als Bestandteil der Verbindungs-URL verwendet 
	 * liest diesen aus der Extension des Plugins aus
	 * @return
	 */
	public String getSubprotocol();

	/**liefert eine neue Connection
	 * @return
	 */
	public Connection getConnection();

	/**liefert die JPA Factory
	 * @return
	 *
	 */
	
	public EntityManagerFactory getJPAEntityManagerFactory();

}