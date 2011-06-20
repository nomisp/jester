package ch.jester.orm;

import java.sql.Connection;
import java.util.HashMap;

import javax.persistence.EntityManagerFactory;

import org.eclipse.core.runtime.IConfigurationElement;

import ch.jester.commonservices.api.preferences.IPreferenceManager;
import ch.jester.commonservices.api.preferences.IPreferenceManagerProvider;


public interface IORMConfiguration extends IPreferenceManagerProvider {

	public IPreferenceManager initializePreferenceManager(String pKey);
	
	public HashMap<String, String> getConfiguration();
	
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


	public String getConnectionOptions();
	
	public String getConnectionString();
	
	public void setConfigElement(IConfigurationElement pElement);
	/**liefert die JPA Factory
	 * @return
	 *
	 */
	public EntityManagerFactory getJPAEntityManagerFactory();
	
	public void setConnectionurl(String pUrl);


}