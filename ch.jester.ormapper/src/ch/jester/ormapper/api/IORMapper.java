package ch.jester.ormapper.api;

import java.sql.Connection;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

public interface IORMapper {
	public EntityManagerFactory getEntityManagerFactory();
	public EntityManager newEntityManager();
	public Connection newConnection();
	public IORMapperSettings getSettings();
}
