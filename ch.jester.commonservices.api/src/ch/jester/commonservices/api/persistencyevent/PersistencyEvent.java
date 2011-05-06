package ch.jester.commonservices.api.persistencyevent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PersistencyEvent {
	public static enum Operation{
		SAVED, DELETED
	}
	private Object mSource;
	private Collection<?> mLoad;
	private Operation mOperation;
	private Class<?> loadClass;
	public PersistencyEvent(Object pSource, Object pLoad, Operation pOps){
		mSource=pSource;
		mLoad=new ArrayList<Object>();
		mOperation = pOps;
		if(pLoad instanceof Collection){
			Collection<?> c = (Collection<?>) pLoad;
			if(!c.isEmpty()){
				loadClass = c.iterator().next().getClass();
			}	
			mLoad = c;
		}else{
			List l = new ArrayList<Object>();
			mLoad = l;
			l.add(pLoad);
			loadClass = pLoad.getClass();
		}
	
	}

	public Class<?> getLoadClass(){
		return loadClass;
	}
	
	public Operation getCRUD(){
		return mOperation;
	}
	
	public Object getSource(){
		return mSource;
	}
	public Collection<?> getLoad(){
		return mLoad;
	}
}
