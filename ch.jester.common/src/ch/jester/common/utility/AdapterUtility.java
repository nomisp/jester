package ch.jester.common.utility;

import org.eclipse.core.runtime.IAdaptable;

public class AdapterUtility {
	@SuppressWarnings("unchecked")
	public static <T> T getAdaptedObject(IAdaptable pAdaptable, Class<T> ptarget){
		return (T) pAdaptable.getAdapter(ptarget);
	}
}
