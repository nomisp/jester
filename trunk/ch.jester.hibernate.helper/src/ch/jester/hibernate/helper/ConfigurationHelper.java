package ch.jester.hibernate.helper;

import java.io.File;
import java.net.URL;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.eclipse.osgi.service.datalocation.Location;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class ConfigurationHelper {
	/**
	 * die Hibernate Configuration
	 */
	private Configuration configuration;	

	/**
	 * liefert die Hibernate Configuration	 
	 */
	public Configuration getConfiguration() {
		if (this.configuration == null) {
			this.configuration = new Configuration();
			this.configuration.setProperty("hibernate.hbm2ddl.auto", "create");
			
			this.configuration.setProperty("hibernate.dialect", 
					this.getSqldialect());
			this.configuration.setProperty("hibernate.connection.driver_class",
					this.getConnectiondriverclass());
			this.configuration.setProperty("hibernate.connection.url", this
					.getConnectionurl());
					
			this.configuration.setProperty("hibernate.connection.username",
					this.getUser());
			this.configuration.setProperty("hibernate.connection.password",
					this.getPassword());
			
			
			this.configuration.setProperty(
					"hibernate.connection.pool_size", "1");
			this.configuration.setProperty(
					"hibernate.connection.autocommit", "true");
//			 alle ?? SQL Statements loggen !
			this.configuration.setProperty("hibernate.show_sql",
					"true");
			this.configuration.setProperty("hibernate.format_sql",
					"true");
			
			
		}
		return this.configuration;
	}
	/**
	 * liefert eine neue Session
	 * @return
	 */
	public Session getSession(){
		SessionFactory factory = getConfiguration().buildSessionFactory();
		Session session=factory.openSession();
		return session;
	}
	/**
	 * liefert das Passwort	
	 */
	public String getPassword() {
		return "";	// hsqldb
//		return "root";	// mysql
	}

	/**
	 * liefert den User
	 * @return
	 */
	public String getUser() {
		return "sa";	// hsqldb
//		return "root";	// mysql
	}

	/**
	 * liefert die Verbindungs-URL
	 * @return
	 */
	public String getConnectionurl() {
		return "jdbc:"+this.getSubprotocol()+"://"+this.getIp()+"/"+this.getDbname();
//		return "jdbc:"+this.getSubprotocol()+":hsql://"+this.getIp()+"/"+this.getDbname();
	}

	/**
	 * liefert die Ip des Datenbankservers
	 * @return
	 */
	public String getIp() {
		Location workingDir = Platform.getInstanceLocation();
		return workingDir.getURL().getFile() + "database";
//		return "localhost";
	}

	/**
	 * liefert den Namen der Datenbank
	 * @return
	 */
	public String getDbname() {
		return "jester";
	}
	
	/**
	 * liefert den Namen der Class des JDBC-Treibers
	 * liest diesen aus der Extension des Plugins aus
	 * @return
	 */
	public String getConnectiondriverclass() {
		return this.getExtensionpointvalue("Configuration","JDBCDriverClass");		
	}

	/**
	 * liefert den Namen der Class des SQL-dialektes
	 * liest diesen aus der Extension des Plugins aus
	 * @return
	 */
	public String getSqldialect() {
		return this.getExtensionpointvalue("Configuration","SQLDialectClass");		
	}
	/**
	 * liefert den Namen des Subprotocols 
	 * dieser wird als Bestandteil der Verbindungs-URL verwendet 
	 * liest diesen aus der Extension des Plugins aus
	 * @return
	 */
	public String getSubprotocol() {
		return this.getExtensionpointvalue("Configuration","Subprotocol");		
	}
	/**
	 * liefert den Wert, der f�r eine ExtensionPoint konfiguriert wurde
	 * @param elementname
	 * @param attributename
	 * @return
	 */
	public String getExtensionpointvalue(String elementname,String attributename ){
		IConfigurationElement element = HibernatehelperPlugin.getDefault().getExtensionPointElement(elementname);
		String value = element.getAttribute(attributename);
		return value;
	}
}
