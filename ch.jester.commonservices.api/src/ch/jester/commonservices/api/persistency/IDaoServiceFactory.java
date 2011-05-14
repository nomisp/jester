package ch.jester.commonservices.api.persistency;


public interface IDaoServiceFactory {

	/**Instanziiert einen neuen IDaoService für die übergebene Klasse
	 * @param <T>
	 * @param objectClass
	 * @return
	 */
	public abstract <T extends IDaoObject> IDaoService<T> getDaoService(Class<T> objectClass);

	/**
	 * Registriert eine implementierende Klasse und deren Service Interface
	 * @param pInterfaceClassName das eigentlich Service Interface
	 * @param class1 die Implementierende Klasse
	 */
	public abstract void addServiceHandling(Class<?> pInterfaceClassName,
			Class<?> class1);

}