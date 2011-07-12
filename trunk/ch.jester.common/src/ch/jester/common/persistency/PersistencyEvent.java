package ch.jester.common.persistency;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import ch.jester.commonservices.api.persistency.IPersistencyEvent;

/**
 * @author  t117221
 */
public class PersistencyEvent implements IPersistencyEvent {

	private Object mSource;
	private Collection<?> mLoad;
	/**
	 * @uml.property  name="mOperation"
	 * @uml.associationEnd  
	 */
	private Operation mOperation;
	/**
	 * @uml.property  name="loadClass"
	 */
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
	/**
	 * @return
	 * @uml.property  name="loadClass"
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
