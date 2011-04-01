package ch.jester.common.utility;

import java.util.HashMap;

import org.eclipse.core.runtime.IAdapterFactory;
import org.eclipse.core.runtime.Platform;

public class DefaultAdapterFactory implements IAdapterFactory{
	private Object mInstance;
	private HashMap<Class<?>, Object> mMap = new HashMap<Class<?>, Object>();
	public void add(Class<?> pType, Object object){
		mMap.put(pType, object);
	}
	public void registerAtPlatform() {
		Platform.getAdapterManager().registerAdapters(this, mInstance.getClass());
		
	}
	public DefaultAdapterFactory(Object pInstance){
		mInstance=pInstance;
	}
	@Override
	public Object getAdapter(Object adaptableObject, Class adapterType) {
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