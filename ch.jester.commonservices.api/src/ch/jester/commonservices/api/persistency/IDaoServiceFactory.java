package ch.jester.commonservices.api.persistency;


public interface IDaoServiceFactory {

	/**Instanziiert einen neuen IDaoService für die übergebene Klasse
	 * @param <T>
	 * @param objectClass
	 * @return
	 */
	public <T extends IEntityObject> IDaoService<T> getDaoService(Class<T> objectClass);

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
	 * Registriert eine IDaoService für ein IDaoObject
	 * @param <T>
	 * @param pClass die implementierende IDaoObject Klasse
	 * @param pServiceClass die Service Klasse
	 */
	public <T extends IEntityObject> void registerDaoService(Class<T> pClass, Class<IDaoService<T>> pServiceClass);

}