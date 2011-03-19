package ch.jester.ormapper.api;


import java.sql.Connection;
import java.util.HashMap;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.osgi.service.component.ComponentContext;

import ch.jester.commonservices.api.importer.IComponentService;

public class ORMapperContainer implements IComponentService<IORMapper>, IORMapper{
	IORMapper mDelegate;
	@Override
	public void start(ComponentContext pComponentContext) {
		System.out.println("Start ORMapperContainer");
	}

	@Override
	public void stop(ComponentContext pComponentContext) {
		
	}

	@Override
	public void bind(IORMapper pT) {
		mDelegate=pT;
	}

	@Override
	public void unbind(IORMapper pT) {
		mDelegate=null;
	}

	@Override
	public EntityManagerFactory getEntityManagerFactory() {
		return mDelegate.getEntityManagerFactory();
	}

	@Override
	public EntityManager newEntityManager() {
		return mDelegate.newEntityManager();
	}

	@Override
	public Connection newConnection() {
		return mDelegate.newConnection();
	}
	
	public IORMapper getORMapper(){
		return mDelegate;
		
	}



	@Override
	public void setORMappingSetting(IORMapperSettings pDef) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public HashMap<String, String> getSettings() {
		// TODO Auto-generated method stub
		return null;
	}

}
