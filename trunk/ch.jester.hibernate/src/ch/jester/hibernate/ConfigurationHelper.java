package ch.jester.hibernate;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Properties;
import java.util.Set;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceUnitUtil;

import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.eclipse.osgi.service.datalocation.Location;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import ch.jester.common.preferences.PreferenceManager;
import ch.jester.common.utility.ExtensionPointSettings;
import ch.jester.commonservices.api.logging.ILogger;
import ch.jester.commonservices.api.logging.ILoggerFactory;
import ch.jester.commonservices.api.preferences.IPreferenceManager;
import ch.jester.commonservices.api.preferences.IPreferenceProperty;
import ch.jester.commonservices.util.ServiceUtility;
import ch.jester.orm.IORMConfiguration;




public class ConfigurationHelper extends ExtensionPointSettings implements IORMConfiguration {
	private PreferenceManager mPrefManager;
	private ILogger mLogger;
	private ServiceUtility mService = new ServiceUtility();
	private IPreferenceProperty mConnectionURLProp;
	public ConfigurationHelper(){
		super(null);
		mPrefManager=new PreferenceManager();
		mLogger = mService.getService(ILoggerFactory.class).getLogger(this.getClass());
		System.out.println("Configuration : "+this);

	}

	private  HashMap<String, String> configuration;
	private  SessionFactory factory;	
	private  EntityManagerFactory emFactory;
	
	//die Hibernate Configuration
	private Configuration hibernateConfiguration;
	private String mConnectionURL;

	
	/**
	 * liefert die Hibernate Configuration	 
	 */
	@Override
	public  synchronized HashMap<String, String> getConfiguration() {
		if (configuration == null) {
		newConfig();

		}
		
		return configuration;
	}

	private void newConfig() {
		configuration = new LinkedHashMap<String, String>();
		//Create Defaults
		mPrefManager.create("hibernate.hbm2ddl.auto", "DDL", "update").setEnabled(false);
		mPrefManager.create("hibernate.dialect","Dialect",getSqldialect());
		mPrefManager.create("hibernate.connection.driver_class","Driver",getConnectiondriverclass());		
		mPrefManager.create("hibernate.connection.pool_size","PoolSize", "1");
		mPrefManager.create("hibernate.connection.autocommit", "Autocommit", false).setEnabled(false);
		mPrefManager.create("hibernate.show_sql",	"Show SQL",false);
		mPrefManager.create("hibernate.format_sql","Format SQL",false);
		mConnectionURLProp = mPrefManager.create("hibernate.connection.url","Connection URL","").setEnabled(false);
		mPrefManager.create("hibernate.c3p0.min_size","Cache.MinSize","5" );
		mPrefManager.create("hibernate.c3p0.max_size","Cache.MaxSize" ,"20" );
		mPrefManager.create("hibernate.c3p0.timeout","Cache.TimeOut" ,"300"  );
		mPrefManager.create("hibernate.c3p0.max_statements","Cache.MaxStatements" ,"50" );
		mPrefManager.create("hibernate.c3p0.idle_test_period","Cache.IdleTestPeriod","300"  );
		
		//mPrefManager.create("javax.persistence.query.timeout","javax.persistence.query.timeout","0"  );
		//mPrefManager.create("javax.persistence.lock.timeout","javax.persistence.lock.timeout","0"  );
		
		mConnectionURLProp.setValue(getConnectionurl());
		
		
		//Put Properties for EP
		HashMap<String, String> map = getAllExtensionPointProperties("Property");
		Iterator<String> it = map.keySet().iterator();
		while(it.hasNext()){
			String key = it.next();
			String value = map.get(key);
			if(key.equals("connection.username")||key.equals("connection.password")){
				IPreferenceProperty prop = mPrefManager.getPropertyByInternalKey(key);
				if(prop==null){
					prop = mPrefManager.create("hibernate."+key, key, value);
				}
			}else{
				mPrefManager.create(key, key, value);
			}
		}
		
		
		
		
		Set<IPreferenceProperty> set = mPrefManager.getProperties();
		mLogger.info("Listing: ****** HIBERNATE CONFIG FOR "+mPrefManager.getPrefixKey()+" ******");
		for(IPreferenceProperty prop:set){
			configuration.put(prop.getInternalKey(), prop.getValue().toString());
			mLogger.info(" - "+prop.getInternalKey()+" = "+prop.getValue());
		}
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
		return getProperties("connection.password");
	}

	@Override
	public  String getUser() {
		return getProperties("connection.username");
	}
	
	public String getConnectionString(){
		return "jdbc:"+getSubprotocol()+"://"+getDefaultPath()+"/"+getDbname();
	}

	@Override
	public String getConnectionurl() {
		if(mConnectionURL == null){
			return getLocalConnection();
		}
		return mConnectionURL;
		//return ORMPlugin.getDefault().getDataBaseManager().getIP();
	}

	public void setConnectionurl(String pUrl){
		Assert.isTrue(configuration==null);
		mConnectionURL = pUrl;
		
	}
	
	@Override
	public String getLocalConnection() {
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
	@Override
	public String getConnectionOptions() {
		String options =  getProperties("connection.options");
		if(options==null){
			options ="";
		}
		return options;
	}
	@SuppressWarnings("deprecation")
	@Override
	public Connection getConnection() {
		return getSession().connection();
	}

	@Override
	public void setConfigElement(IConfigurationElement pElement) {
		super.setConfigurationElement(pElement);
		String id = super.getConfigurationElement().getContributor().getName();
		mPrefManager.setPrefixKey(id);
		
	}

	@Override
	public IPreferenceManager getPreferenceManager(String pKey) {
		return mPrefManager.checkId(pKey);
	}

	@Override
	public IPreferenceManager initializePreferenceManager(String pKey) {
		mPrefManager.setPrefixKey(pKey);
		this.getConfiguration();
		return mPrefManager;
	}



	
}
