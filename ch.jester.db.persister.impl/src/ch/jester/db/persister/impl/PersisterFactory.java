package ch.jester.db.persister.impl;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;

import org.osgi.framework.Bundle;
import org.osgi.framework.ServiceFactory;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.component.ComponentContext;


import ch.jester.commonservices.api.bundle.IActivationContext;
import ch.jester.commonservices.api.components.IComponentService;
import ch.jester.commonservices.api.logging.ILoggerFactory;
import ch.jester.commonservices.api.persistency.IDaoObject;
import ch.jester.commonservices.api.persistency.IDaoService;
import ch.jester.commonservices.api.persistency.IDaoServiceFactory;
import ch.jester.commonservices.util.ServiceUtility;

import ch.jester.dao.ICategoryDao;
import ch.jester.dao.IPlayerDao;
import ch.jester.dao.IRoundDao;
import ch.jester.dao.ITournamentDao;

/**
 * Implementiert eine ServiceFactory und stellt die DAO Implementierungen zur Verfügung.
 * Die Klasse wird als Komponente mit DS gestartet und registriert sich selbst als Factory für die entsprechenden
 * DAO Services.
 * Die Factory gibt immer eine neue Service Instanz zurück. Damit wird das DS/OSGI Caching pro Bundle umgangen, so dass
 * der DAO Service ansich nicht Threadsafe sein muss.
 *  
 *
 */
public class PersisterFactory implements ServiceFactory, IComponentService<Object>, IDaoServiceFactory{
	private ILoggerFactory mLoggerFactory;
	private ServiceUtility mServiceUtility;
	private HashMap<String, Class<?>> mServiceInterfaceRegistry = new HashMap<String, Class<?>>();
	private HashMap<Class<?>, Class<?>> mDaoObjectClassRegistry = new HashMap<Class<?>, Class<?>>();
	private IActivationContext<?> mActivationContext;
	public PersisterFactory(){
		
	}
	
	@Override
	public Object getService(Bundle bundle, ServiceRegistration registration) {
		String objectClass = ((String[])registration.getReference().getProperty("objectClass"))[0];
		Class<?> clz = mServiceInterfaceRegistry.get(objectClass);
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
	
	/* (non-Javadoc)
	 * @see ch.jester.db.persister.impl.IDaoServiceFactory#getDaoService(java.lang.Class)
	 */
	@Override
	public <T extends IDaoObject> IDaoService<T> getDaoService(Class<T> objectClass){
		//wurde ev. ein ServiceInterface als Suchparameter übergen?
		Class<?> clz = mServiceInterfaceRegistry.get(objectClass.getCanonicalName());
		if(clz==null){
			//wurde die DAO Klasse selber übergeben
			clz = mDaoObjectClassRegistry.get(objectClass);
		}
		
		if(clz!=null){
		try {
			return (IDaoService<T>) clz.newInstance();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generaated catch block
			e.printStackTrace();
		}}
		
		
		return (IDaoService<T>)  new GenericPersister<T>(objectClass);
	}
	

	@Override
	public void ungetService(Bundle bundle, ServiceRegistration registration,
			Object service) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see ch.jester.db.persister.impl.IDaoServiceFactory#addServiceHandling(java.lang.Class, java.lang.Class)
	 */
	@Override
	public void addServiceHandling(Class<?> pInterfaceClassName, Class<?> class1) {
		mServiceInterfaceRegistry.put(pInterfaceClassName.getName(), class1);
		mServiceUtility.registerServiceFactory(pInterfaceClassName, this);
		Type actualTypeArgument = ((ParameterizedType) class1.getGenericSuperclass()).getActualTypeArguments()[0];
		Class<?> mClz = (Class<?>) actualTypeArgument;
		mDaoObjectClassRegistry.put(mClz, class1);
		
	}

	@Override
	public void start(ComponentContext pComponentContext) {
		mActivationContext = Activator.getDefault().getActivationContext();
		mLoggerFactory.getLogger(this.getClass()).debug("PersisterFactory started");
		mServiceUtility=mActivationContext.getServiceUtil();
		//TODO Umschreiben: wir wollen die Interfaces loswerden
		addServiceHandling(IPlayerDao.class, DBPlayerPersister.class);
		addServiceHandling(ITournamentDao.class, DBTournamentPersister.class);
		addServiceHandling(ICategoryDao.class, DBCategoryPersister.class);
		addServiceHandling(IRoundDao.class, DBRoundPersister.class);
	
	}

	@Override
	public void stop(ComponentContext pComponentContext) {
		// TODO Auto-generated method stub
		
	}
	
	/**DS Dependency Injection<br>
	 * Da dies eine Komponente ist, kann der ILogger nicht über den IActivationContext bezogen werden,
	 * denn die Komponente für den ILogger kann unter umständen noch nicht gestartet worden sein.<br>
	 * Also wird die Zuteilung der Factory DS überlassen.
	 * @param pFactory
	 */
	public void bindLoggerFactory(ILoggerFactory pFactory){
		mLoggerFactory=pFactory;
		}

	@Override
	public void bind(Object pT) {
		System.out.println("bind "+pT);
		
	}

	@Override
	public void unbind(Object pT) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public <T extends IDaoObject> void registerDaoService(Class<T> pClass,
			Class<IDaoService<T>> pServiceClass) {
		mDaoObjectClassRegistry.put(pClass, pServiceClass);
	}

}
