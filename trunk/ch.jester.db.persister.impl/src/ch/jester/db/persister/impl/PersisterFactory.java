package ch.jester.db.persister.impl;

import java.util.HashMap;

import org.osgi.framework.Bundle;
import org.osgi.framework.ServiceFactory;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.component.ComponentContext;


import ch.jester.common.activator.IActivationContext;
import ch.jester.common.utility.ServiceUtility;
import ch.jester.commonservices.api.IComponentService;
import ch.jester.dao.IPlayerPersister;

public class PersisterFactory implements ServiceFactory, IComponentService<Object>{
	private ServiceUtility mServiceUtility;
	private HashMap<String, Class<?>> mRegistry = new HashMap<String, Class<?>>();
	private IActivationContext<?> mActivationContext;
	public PersisterFactory(){
		
	}
	
	@Override
	public Object getService(Bundle bundle, ServiceRegistration registration) {
		String objectClass = ((String[])registration.getReference().getProperty("objectClass"))[0];
		Class<?> clz = mRegistry.get(objectClass);
		try {
			return clz.newInstance();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	

	@Override
	public void ungetService(Bundle bundle, ServiceRegistration registration,
			Object service) {
		// TODO Auto-generated method stub
		
	}

	public void addServiceHandling(Class<?> pInterfaceClassName, Class<?> class1) {
		mRegistry.put(pInterfaceClassName.getName(), class1);
		mServiceUtility.registerServiceFactory(pInterfaceClassName, this);
		
	}

	@Override
	public void start(ComponentContext pComponentContext) {
		mActivationContext = Activator.getDefault().getActivationContext();
		mActivationContext.getLogger().debug("PersisterFactory started");
		mServiceUtility=mActivationContext.getServiceUtil();
		addServiceHandling(IPlayerPersister.class, DBPlayerPersister.class);
		
	}

	@Override
	public void stop(ComponentContext pComponentContext) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void bind(Object pT) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void unbind(Object pT) {
		// TODO Auto-generated method stub
		
	}

}
