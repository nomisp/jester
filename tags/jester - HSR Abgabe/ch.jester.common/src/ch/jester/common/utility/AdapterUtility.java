package ch.jester.common.utility;

import org.eclipse.core.runtime.IAdaptable;

/**
 * Utility Klasse für IAdaptable.
 *
 */
public class AdapterUtility {
	/**
	 * Ruft {@link IAdaptable#getAdapter(Class)} auf <code>pAdaptable</code> auf und gibt das 
	 * entsprechende Objekt oder null zurück.
	 * @param <T>
	 * @param pAdaptable
	 * @param ptarget
	 * @return ein Objekt, welches für <code>Class<T> ptarget</code> registriert wurde, oder null
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getAdaptedObject(IAdaptable pAdaptable, Class<T> ptarget){
		return (T) pAdaptable.getAdapter(ptarget);
	}
}
