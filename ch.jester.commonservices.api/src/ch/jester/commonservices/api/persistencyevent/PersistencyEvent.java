package ch.jester.commonservices.api.persistencyevent;

import java.util.ArrayList;
import java.util.Collection;

public class PersistencyEvent {
	public enum Operation{
		SAVED, DELETED
	}
	private Object mSource;
	private Collection<Object> mLoad;
	private Operation mOperation;
	public PersistencyEvent(Object pSource, Collection<Object> pLoad){
		mSource=pSource;
		mLoad=pLoad;
	}
	public PersistencyEvent(Object pSource, Object pLoad, Operation pOps){
		mSource=pSource;
		mLoad=new ArrayList<Object>();
		mLoad.add(pLoad);
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
