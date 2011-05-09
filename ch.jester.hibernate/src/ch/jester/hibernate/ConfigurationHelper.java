package ch.jester.hibernate;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Properties;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.eclipse.osgi.service.datalocation.Location;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import ch.jester.common.utility.ExtensionPointSettings;
import ch.jester.orm.IORMConfiguration;
import ch.jester.orm.ORMPlugin;


public class ConfigurationHelper extends ExtensionPointSettings implements IORMConfiguration {
	public ConfigurationHelper(){
		super(null);
	}
	
	private  HashMap<String, String> configuration;
	private  SessionFactory factory;	
	private  EntityManagerFactory emFactory;
	
	//die Hibernate Configuration
	private Configuration hibernateConfiguration;

	/**
	 * liefert die Hibernate Configuration	 
	 */
	  synchronized HashMap<String, String> getConfiguration() {
		if (configuration == null) {
			configuration = new HashMap<String, String>();

			configuration.put("hibernate.hbm2ddl.auto", "update");
			configuration.put("hibernate.dialect", getSqldialect());
			configuration.put("hibernate.connection.driver_class",getConnectiondriverclass());
		   // configuration.put("hibernate.connection.url", getConnectionurl()+";hsqldb.default_table_type=cached;hsqldb.tx=mvcc");		
			configuration.put("hibernate.connection.pool_size", "1");
			configuration.put("hibernate.connection.autocommit", "false");
			configuration.put("hibernate.show_sql",	"false");
			configuration.put("hibernate.format_sql","false");
			configuration.put("hibernate.connection.url",getConnectionurl());
			
			
			configuration.put("hibernate.c3p0.min_size" ,"5" );
			configuration.put("hibernate.c3p0.max_size" ,"20" );
			configuration.put("hibernate.c3p0.timeout" ,"300"  );
			configuration.put("hibernate.c3p0.max_statements" ,"50" );
			configuration.put("hibernate.c3p0.idle_test_period","3000"  );
	 
			
			configuration.putAll(getAllProperties("Property"));
		//	configuration.setProperty("hibernate.connection.driver_class","org.hsqldb.jdbcDriver");
			//configuration.setProperty("hibernate.connection.url","jdbc:hsqldb:hsql://localhost/jester");

			
		}
		
		return configuration;
	}

	@Override
	public EntityManagerFactory getJPAEntityManagerFactory(){
		if(emFactory==null){
			synchronized (ConfigurationHelper.class) {
				if(emFactory==null){
					emFactory = Persistence.createEntityManagerFactory("jester", getConfiguration());
				}

			}
		}
		return emFactory;
	}
	/**liefert eine neue Hibernate Configuration
	 * @return
	 */
	private Configuration getHibernateConfiguration(){
		if(hibernateConfiguration==null){
			Properties props = new Properties();
			Configuration config = new Configuration();
			props.putAll(getConfiguration());
			config.setProperties(props);
			hibernateConfiguration=config;
		}
		return hibernateConfiguration;
		
	}
	  
	/**
	 * liefert eine neue Session
	 * @return
	 */
	 synchronized Session getSession(){
		if(factory==null){
			factory = getHibernateConfiguration().buildSessionFactory();
		}
		Session session=factory.openSession();
		return session;
	}

	@Override
	public  String getPassword() {
		return getProperties("Configuration", "hibernate.connection.password");
	}

	@Override
	public  String getUser() {
		return getProperties("Configuration", "hibernate.connection.username");
	}

	@Override
	public   String getConnectionurl() {
		return ORMPlugin.getDefault().getDataBaseManager().getIP();
	}

	@Override
	public   String getLocalConnection() {
	return "jdbc:"+getSubprotocol()+"://"+getDefaultPath()+"/"+getDbname();
	}

	@Override
	public  String getDefaultPath() {
		Location workingDir = Platform.getInstanceLocation();
		return workingDir.getURL().getFile() + "database";
	}

	@Override
	public  String getDbname() {
		return getExtensionPointValueFromElement("DatabaseName");		
	}
	
	@Override
	public  String getConnectiondriverclass() {
		return getExtensionPointValueFromElement("JDBCDriverClass");		
	}

	@Override
	public  String getSqldialect() {
		return getExtensionPointValueFromElement("SQLDialectClass");		
	}

	@Override
	public  String getSubprotocol() {
		return getExtensionPointValueFromElement("Subprotocol");		
	}

	@SuppressWarnings("deprecation")
	@Override
	public Connection getConnection() {
		return getSession().connection();
	}

	@Override
	public void setConfigElement(IConfigurationElement pElement) {
		super.setConfigurationElement(pElement);
		
	}

	
}
