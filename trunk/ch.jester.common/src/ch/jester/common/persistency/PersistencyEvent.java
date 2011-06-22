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

	/* (non-Javadoc)
	 * @see ch.jester.common.persistency.util.IPersistencyEvent#getLoadClass()
	 */
	@Override
	public Class<?> getLoadClass(){
		return loadClass;
	}
	
	/* (non-Javadoc)
	 * @see ch.jester.common.persistency.util.IPersistencyEvent#getCRUD()
	 */
	@Override
	public Operation getCRUD(){
		return mOperation;
	}
	
	/* (non-Javadoc)
	 * @see ch.jester.common.persistency.util.IPersistencyEvent#getSource()
	 */
	@Override
	public Object getSource(){
		return mSource;
	}
	/* (non-Javadoc)
	 * @see ch.jester.common.persistency.util.IPersistencyEvent#getLoad()
	 */
	@Override
	public Collection<?> getLoad(){
		return mLoad;
	}
}
