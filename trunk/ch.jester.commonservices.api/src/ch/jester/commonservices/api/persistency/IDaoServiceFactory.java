package ch.jester.commonservices.api.persistency;


/**
 * Factory Interface welche IDaoServices erzeugen kann.
 *
 */
public interface IDaoServiceFactory {

	/**Instanziiert einen neuen IDaoService für die übergebene Entitätsklasse
	 * @param <T>
	 * @param objectClass
	 * @return
	 */
	public <T extends IEntityObject> IDaoService<T> getDaoServiceByEntity(Class<T> objectClass);
	/**
	 * Instanziiert einen neuen IDaoService für die übergebene Serviceklasse
	 * @param <T>
	 * @param objectClass
	 * @return
	 */
	public <T extends IDaoService<?>> T getDaoServiceByServiceInterface(Class<T> objectClass);

	/**
	 *
	 * Registriert eine implementierende Klasse und deren Service Interface
	 * @deprecated use {@link IDaoServiceFactory#registerDaoService(Class, Class)}
	 * @param pInterfaceClassName das eigentlich Service Interface
	 * @param class1 die Implementierende Klasse
	 */
	@Deprecated
	public  void addServiceHandling(Class<?> pInterfaceClassName,
			Class<?> class1);
	
	/**
	 * Registriert eine IDaoService für ein IEntityObject
	 * @param <T>
	 * @param pClass die implementierende IEntityObject Klasse
	 * @param pServiceClass die Service Klasse
	 */
	public <T extends IEntityObject> void registerDaoService(Class<T> pClass, Class<IDaoService<T>> pServiceClass);

	/**
	 * Adaptiert den übergebenen Service, so dass ein private EntityManager verwendet wird
	 * @param <T>
	 * @param pService
	 * @return
	 */
	public <T extends IEntityObject> IPrivateContextDaoService<T> adaptPrivate(IDaoService<T> pService);
}