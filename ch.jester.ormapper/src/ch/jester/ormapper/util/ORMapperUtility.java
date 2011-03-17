package ch.jester.ormapper.util;

import java.util.HashMap;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import ch.jester.common.utility.ServiceUtility;
import ch.jester.ormapper.api.IORMapper;
import ch.jester.ormapper.api.ORMapperContainer;

public class ORMapperUtility {
	private static ServiceUtility mUtil = new ServiceUtility();
	public static IORMapper getORMapper(){
		EntityManagerFactory factory= Persistence.createEntityManagerFactory("jester", getDefaultConfig());
		return mUtil.getService(ORMapperContainer.class).getORMapper();
	}

	public static HashMap<String, String> getDefaultConfig(){
		HashMap<String, String> configurationProperties = new HashMap<String, String> ();
		
		configurationProperties.put("javax.persistence.provider", "org.hibernate.ejb.HibernatePersistence");
		
		configurationProperties.put("hibernate.hbm2ddl.auto", "update");
		//configurationProperties.put("hibernate.dialect", getSqldialect());
		//configurationProperties.put("hibernate.connection.driver_class",getConnectiondriverclass());
	   // configuration.put("hibernate.connection.url", getConnectionurl()+";hsqldb.default_table_type=cached;hsqldb.tx=mvcc");		
		configurationProperties.put("hibernate.connection.pool_size", "1");
		configurationProperties.put("hibernate.connection.autocommit", "true");
		configurationProperties.put("hibernate.show_sql",	"false");
		configurationProperties.put("hibernate.format_sql","false");
		//configurationProperties.put("hibernate.connection.url",getConnectionurl());
	//	configurationProperties.putAll(getAllProperties("Configuration"));
		return configurationProperties;
	}
}
