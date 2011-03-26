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

/**
 * Implementiert eine ServiceFactory und stellt die DAO Implementierungen zur Verfügung.
 * Die Klasse wird als Komponente mit DS gestartet und registriert sich selbst als Factory für die entsprechenden
 * DAO Services.
 * Die Factory gibt immer eine neue Service Instanz zurück. Damit wird das DS/OSGI Caching pro Bundle umgangen, so dass
 * der DAO Service ansich nicht Threadsafe sein muss.
 *  
 *
 */
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

	/**
	 * Registriert eine implementierende Klasse und deren Service Interface
	 * @param pInterfaceClassName das eigentlich Service Interface
	 * @param class1 die Implementierende Klasse
	 */
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
