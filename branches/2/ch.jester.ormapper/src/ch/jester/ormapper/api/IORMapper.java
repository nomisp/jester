package ch.jester.ormapper.api;

import java.sql.Connection;
import java.util.HashMap;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

public interface IORMapper {
	public EntityManagerFactory getEntityManagerFactory();
	public EntityManager newEntityManager();
	public Connection newConnection();
	public void setORMappingSetting(IORMapperSettings pDef);
	public HashMap<String, String> getSettings();
}
