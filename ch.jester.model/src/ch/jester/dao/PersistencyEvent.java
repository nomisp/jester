package ch.jester.dao;

public class PersistencyEvent {
	private Object mSource, mLoad;
	public PersistencyEvent(Object pSource, Object pLoad){
		mSource=pSource;
		mLoad=pLoad;
	}
	public Object getSource(){
		return mSource;
	}
	public Object getLoad(){
		return mLoad;
	}
}
