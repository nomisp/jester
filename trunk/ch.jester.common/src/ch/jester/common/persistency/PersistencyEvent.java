package ch.jester.common.persistency;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import ch.jester.commonservices.api.persistency.IPersistencyEvent;

public class PersistencyEvent implements IPersistencyEvent {

	private Object mSource;
	private Collection<?> mLoad;
	private Operation mOperation;
	private Class<?> loadClass;
	@SuppressWarnings("unchecked")
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
			@SuppressWarnings("rawtypes")
			List l = new ArrayList<Object>();
			mLoad = l;
			l.add(pLoad);
			loadClass = pLoad.getClass();
		}
	
	}
	@Override
	public Class<?> getLoadClass(){
		return loadClass;
	}
	@Override
	public Operation getCRUD(){
		return mOperation;
	}
	@Override
	public Object getSource(){
		return mSource;
	}
	@Override
	public Collection<?> getLoad(){
		return mLoad;
	}
}
