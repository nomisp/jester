package ch.jester.common.utility.persistency;

import java.util.ArrayList;
import java.util.Collection;

public class PersistencyEvent {
	private Object mSource;
	private Collection<Object> mLoad;
	public PersistencyEvent(Object pSource, Collection<Object> pLoad){
		mSource=pSource;
		mLoad=pLoad;
	}
	public PersistencyEvent(Object pSource, Object pLoad){
		mSource=pSource;
		mLoad=new ArrayList<Object>();
		mLoad.add(pLoad);
	}
	public Object getSource(){
		return mSource;
	}
	public Collection<?> getLoad(){
		return mLoad;
	}
}
