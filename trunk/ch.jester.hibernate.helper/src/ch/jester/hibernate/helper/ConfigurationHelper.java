package ch.jester.hibernate.helper;

import java.util.Properties;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.eclipse.osgi.service.datalocation.Location;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import ch.jester.common.utility.ExtensionPointUtil;
import ch.jester.model.Category;
import ch.jester.model.Club;
import ch.jester.model.Pairing;
import ch.jester.model.Player;
import ch.jester.model.Round;
import ch.jester.model.Tournament;

public class ConfigurationHelper {
	private ConfigurationHelper(){
		
	}
	/**
	 * die Hibernate Configuration
	 */
	private static Configuration configuration;
	private static SessionFactory factory;	
	private static EntityManagerFactory emFactory;

	/**
	 * liefert die Hibernate Configuration	 
	 */
	 static synchronized Configuration getConfiguration() {
		if (configuration == null) {
			configuration = new Configuration();
			//configuration.addPackage("ch.jester.model");
			
			
			configuration.addAnnotatedClass(Category.class);
			configuration.addAnnotatedClass(Round.class);
			configuration.addAnnotatedClass(Pairing.class);
			configuration.addAnnotatedClass(Club.class);
			configuration.addAnnotatedClass(Tournament.class);
			configuration.addAnnotatedClass(Player.class);

			configuration.setProperty("hibernate.hbm2ddl.auto", "update");
			configuration.setProperty("hibernate.dialect", getSqldialect());
			configuration.setProperty("hibernate.connection.driver_class",getConnectiondriverclass());
		   // configuration.setProperty("hibernate.connection.url", getConnectionurl()+";hsqldb.default_table_type=cached;hsqldb.tx=mvcc");		
			configuration.setProperty("hibernate.connection.pool_size", "1");
			configuration.setProperty("hibernate.connection.autocommit", "true");
			configuration.setProperty("hibernate.show_sql",	"false");
			configuration.setProperty("hibernate.format_sql","false");
			configuration.setProperty("hibernate.connection.url",getConnectionurl());
			configuration.addProperties(getAllProperties("Configuration"));
		//	configuration.setProperty("hibernate.connection.driver_class","org.hsqldb.jdbcDriver");
			//configuration.setProperty("hibernate.connection.url","jdbc:hsqldb:hsql://localhost/jester");

			
		}
		
		return configuration;
	}

	 static synchronized EntityManagerFactory getJPAEntitManagerFactor(){
		if(emFactory==null){
			if(configuration ==null){
				getConfiguration();
			}

			emFactory = Persistence.createEntityManagerFactory("jester", configuration.getProperties());
		}
		return emFactory;
	}
	/**
	 * liefert eine neue Session
	 * @return
	 */
	static synchronized Session getSession(){
		if(factory==null){
			factory = getConfiguration().buildSessionFactory();
		}
		Session session=factory.openSession();
		return session;
	}
	/**
	 * liefert das Passwort	
	 */
	public static String getPassword() {
		return getProperties("Configuration", "hibernate.connection.password");
	}

	/**
	 * liefert den User
	 * @return
	 */
	public static String getUser() {
		return getProperties("Configuration", "hibernate.connection.username");
	}

	/**
	 * liefert die Verbindungs-URL
	 * @return
	 */
	public static  String getConnectionurl() {
		return HibernatehelperPlugin.getDefault().getDataBaseManager().getIP();
	//	return "jdbc:"+getSubprotocol()+"://"+getPath()+"/"+getDbname();
//		return "jdbc:"+this.getSubprotocol()+":hsql://"+this.getIp()+"/"+this.getDbname();
	}
	public static  String getLocalConnection() {
		//return HibernatehelperPlugin.getDefault().getDataBaseManager().getIP();
	return "jdbc:"+getSubprotocol()+"://"+getDefaultPath()+"/"+getDbname();
//		return "jdbc:"+this.getSubprotocol()+":hsql://"+this.getIp()+"/"+this.getDbname();
	}

	/**
	 * liefert die Ip des Datenbankservers
	 * @return
	 */
	public static String getDefaultPath() {
		Location workingDir = Platform.getInstanceLocation();
		return workingDir.getURL().getFile() + "database";
//		return "localhost";
	}

	/**
	 * liefert den Namen der Datenbank
	 * @return
	 */
	public static String getDbname() {
		return "jester";
	}
	
	/**
	 * liefert den Namen der Class des JDBC-Treibers
	 * liest diesen aus der Extension des Plugins aus
	 * @return
	 */
	public static String getConnectiondriverclass() {
		return getExtensionpointvalue("Configuration","JDBCDriverClass");		
	}

	/**
	 * liefert den Namen der Class des SQL-dialektes
	 * liest diesen aus der Extension des Plugins aus
	 * @return
	 */
	public static String getSqldialect() {
		return getExtensionpointvalue("Configuration","SQLDialectClass");		
	}
	/**
	 * liefert den Namen des Subprotocols 
	 * dieser wird als Bestandteil der Verbindungs-URL verwendet 
	 * liest diesen aus der Extension des Plugins aus
	 * @return
	 */
	public static String getSubprotocol() {
		return getExtensionpointvalue("Configuration","Subprotocol");		
	}
	/**
	 * liefert den Wert, der für eine ExtensionPoint konfiguriert wurde
	 * @param elementname
	 * @param attributename
	 * @return
	 */
	private static String getExtensionpointvalue(String elementname,String attributename ){
		IConfigurationElement element = ExtensionPointUtil.getExtensionPointElement(HibernatehelperPlugin.getDefault().getPluginId(), elementname);
		String value = element.getAttribute(attributename);
		return value;
	}
	private static String getProperties(String elementname,String attributename ){
		IConfigurationElement element = ExtensionPointUtil.getExtensionPointElement(HibernatehelperPlugin.getDefault().getPluginId(), elementname);
		IConfigurationElement[] children = element.getChildren("Property");
	
		for(IConfigurationElement child:children){
			if(child.getAttribute("name").equals(attributename)){
				return child.getAttribute("value")==null?"":child.getAttribute("value");
			}
		}
		return null;
	}
	private static Properties getAllProperties(String parentName){
		IConfigurationElement element = ExtensionPointUtil.getExtensionPointElement(HibernatehelperPlugin.getDefault().getPluginId(), parentName);
		IConfigurationElement[] children = element.getChildren("Property");
		Properties map = new Properties();
		for(IConfigurationElement child:children){
			map.put(child.getAttribute("name"), child.getAttribute("value")==null?"":child.getAttribute("value"));
		}
		return map;
	}

	
}
