package ch.jester.commonservices.util;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceFactory;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;

import ch.jester.commonservices.api.persistency.IEntityObject;
import ch.jester.commonservices.api.persistency.IDaoService;
import ch.jester.commonservices.api.persistency.IDaoServiceFactory;
import ch.jester.commonservices.internal.Activator;


/**
 * Hilfsklasse, welche die benutzten ServiceTrackers zwischenspeichert, und den
 * Zugriff auf Services vereinfacht.
 * 
 */
public class ServiceUtility {
	private static Object GLOBAL_LOCK = new Object();
	private BundleContext mContext;
	private HashMap<Class<?>, ServiceTracker> mTrackerMap = new HashMap<Class<?>, ServiceTracker>();

	/**
	 *
	 * @param pContext
	 */
	public ServiceUtility(BundleContext pContext) {
		mContext = pContext;
	}
	
	/**
	 * Benutzt den BundleContext vom eigenen Plugin
	 */
	public ServiceUtility(){
		this(Activator.getDefault().getBundleContext());
	}

	/**
	 * Versucht einen Service aus der ServiceRegistry zu bekommen. <br>
	 * IDaoService Requests werden zu {@link ServiceUtility#getDaoServiceByServiceInterface(Class)} weitergeben.
	 * @param <T>
	 * @param Interface
	 * @return Ein Objekt welches das übergebene Interface implementiert oder
	 *         null
	 */
	@SuppressWarnings("unchecked")
	public <T> T getService(Class<T> pServiceInterface) {
		if(IDaoService.class.isAssignableFrom(pServiceInterface)){
			Class<IDaoService<IEntityObject>> clz = (Class<IDaoService<IEntityObject>>) pServiceInterface;
			return (T) getDaoServiceByServiceInterface(clz);
		}
		ServiceTracker tracker = getServiceTracker(pServiceInterface, true);
		Object service = tracker.getService();
		return (T) service;
	}

	/**
	 * Gibt alle Services mit entsprechendem Interface zurück
	 * 
	 * @param <T>
	 *            Typ
	 * @param pServiceInterface
	 *            Interface des Services
	 * @return List<T>
	 */
	@SuppressWarnings("unchecked")
	public <T> List<T> getServices(Class<T> pServiceInterface) {
		ServiceTracker tracker = getServiceTracker(pServiceInterface, true);
		Object[] service = tracker.getServices();
		List<T> list = new ArrayList<T>();
		if(service==null){return list;}
		for (Object o : service) {
			list.add((T) o);
		}
		return list;
	}

	/**
	 * Überprüft ob bereits ein ServiceTracker für die übergebene Klasse
	 * existiert. Falls nicht, wird ein neuer ServiceTracker mit der übergebenen
	 * Klasse als Filter erstellt und geöffnet.
	 * 
	 * @param pServiceInterface
	 * @param TrackerCreation
	 * @return
	 */
	private ServiceTracker getServiceTracker(Class<?> pServiceInterface,
			boolean pCreate) {
		ServiceTracker tracker = mTrackerMap.get(pServiceInterface);
		if (tracker == null && pCreate) {
			tracker = createTracker(pServiceInterface);
			tracker.open();
			mTrackerMap.put(pServiceInterface, tracker);
		}
		return tracker;
	}
	
	/**Erstellt einen neuen Tracker
	 * @param pServiceInterface
	 * @return
	 */
	private ServiceTracker createTracker(Class<?> pServiceInterface){
		return new ServiceTracker(mContext, pServiceInterface.getName(),
				null);
	}

	/**
	 * Schliesst alle ServiceTracker
	 */
	public void closeAllTrackers() {
		Iterator<Class<?>> mapIterator = mTrackerMap.keySet().iterator();
		while (mapIterator.hasNext()) {
			mTrackerMap.get(mapIterator.next()).close();
		}
	}

	/**
	 * Registriert ein Objekt als Service
	 * 
	 * @param <T>
	 * @param pInterface
	 *            Das Interface unter welchem die Registration erfolgt
	 * @param pService
	 *            das Objekt
	 * @param pConfig
	 *            die Konfiguration
	 */
	public <T> void registerService(Class<T> pInterface, T pService,
			Dictionary<?, ?> pConfig) {
		this.mContext.registerService(pInterface.getName(), pService, pConfig);
	}


	private void registerServiceFactory(Class<?> pInterface, ServiceFactory pFactory,
			Dictionary<String, String> dict) {
		this.mContext.registerService(pInterface.getName(), pFactory, dict);
		
	}
	/**
	 * Registriert ein Objekt als Service, mit leerer Konfiguration
	 * 
	 * @param <T>
	 * @param pInterface
	 *            Das Interface unter welchem die Registration erfolgt
	 * @param pService
	 *            das Objekt
	 */
	public <T> void registerService(Class<T> pInterface, T pService) {
		Dictionary<String, String> dict = new Hashtable<String, String>();
		registerService(pInterface, pService, dict);
	}

	/**Registriert eine Klasse als ServiceInterface und benutzt die Factory zur
	 * Instanziierung
	 * @param pInterface
	 * @param pFactory 
	 */
	public void registerServiceFactory(Class<?> pInterface,
			ServiceFactory pFactory) {
		Dictionary<String, String> dict = new Hashtable<String, String>();
		registerServiceFactory(pInterface, pFactory, dict);
		
	}

	/**
	 * Versucht eine brandneue ServiceInstanz zu bekommen.
	 * Dies hängt allerdings mit der Bereitstellung des Services zusammen, so dass
	 * keine Garanntie dafür besteht.
	 * @deprecated use {@link ServiceUtility#getDaoServiceByEntity(Class)}
	 * @param <T>
	 * @param pServiceInterface
	 * @return
	 */
	@Deprecated 
	@SuppressWarnings("unchecked")
	private <T> T getExclusiveService(Class<T> pServiceInterface) {
		synchronized(GLOBAL_LOCK){
		ServiceTracker tracker = createTracker(pServiceInterface);
		tracker.open();
		Object service = tracker.getService();
		ServiceReference ref = tracker.getServiceReference();
		mContext.ungetService(ref);
		tracker.close();
		tracker=null;
		return (T) service;
		}
	}

	/**
	 * Holt eine brandneue Instanz eines IDaoServices.
	 * 
	 * @param <T>
	 * @param pClass eine konkrete Klasse, welche IDaoServiceObject implementiert
	 * @return eine Serviceimplementierung
	 */
	public <T extends IEntityObject> IDaoService<T> getDaoServiceByEntity(Class<T> pClass){
		IDaoServiceFactory factory = getService(IDaoServiceFactory.class);
		if(factory==null){return null;}
		return factory.getDaoServiceByEntity(pClass);
	}
	
	/**
	 * Holt eine brandneue Instanz eines IDaoServices.
	 * 
	 * @param <T>
	 * @param pClass eine konkrete Klasse, welche IDaoServiceObject implementiert
	 * @return eine Serviceimplementierung
	 */
	public <T extends IDaoService<?>> T getDaoServiceByServiceInterface(Class<T> pClass){
		IDaoServiceFactory factory = getService(IDaoServiceFactory.class);
		if(factory==null){return null;}
		return factory.getDaoServiceByServiceInterface(pClass);
	}

}
