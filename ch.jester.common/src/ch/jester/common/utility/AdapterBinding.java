package ch.jester.common.utility;

import java.util.HashMap;

import org.eclipse.core.runtime.IAdapterFactory;
import org.eclipse.core.runtime.Platform;

/**
 * Hilfklasse für die Registrierung von IAdaptable
 *
 */
public class AdapterBinding implements IAdapterFactory{
	private Object mInstance;
	private HashMap<Class<?>, Object> mMap = new HashMap<Class<?>, Object>();
	/**
	 * Assoziert die Klasse mit dem eigentlichen Objekt
	 * @param pType
	 * @param object
	 */
	public void add(Class<?> pType, Object object){
		mMap.put(pType, object);
	}
	/**
	 * Registriert das Binding als IAdapterFactory an der Platform.
	 */
	public void bind() {
		Platform.getAdapterManager().registerAdapters(this, mInstance.getClass());
		
	}
	/**
	 * Constructor.
	 * Das übergebene Objekt wird als Suchkriterium der Platform benutzt.
	 * <br>
	 * Die mit {@link AdapterBinding#add(Class, Object)} Registrationen können darauf für das Objekt <code>pInstance</code>
	 * benutzt werden.
	 * @param pInstance
	 */
	public AdapterBinding(Object pInstance){
		mInstance=pInstance;
	}
	@Override
	public Object getAdapter(Object adaptableObject, @SuppressWarnings("rawtypes") Class adapterType) {
		Object objectForClass = mMap.get(adapterType);
		if(adaptableObject==mInstance){
			return objectForClass;
		}
		return null;
	}

	@Override
	public Class<?>[] getAdapterList() {
		return mMap.keySet().toArray(new Class[mMap.keySet().size()]);
	}
	
}