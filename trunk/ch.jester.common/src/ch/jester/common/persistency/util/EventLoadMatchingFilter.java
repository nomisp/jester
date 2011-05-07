package ch.jester.common.persistency.util;

import ch.jester.commonservices.api.persistency.IPersistencyEvent;
import ch.jester.commonservices.api.persistency.IPersistencyFilter;

public class EventLoadMatchingFilter extends ChainedPersistencyFilter{
	private Class<?> mClass;
	public EventLoadMatchingFilter(Class<?> pClz){
		super();
		mClass = pClz;
	}
	public EventLoadMatchingFilter(Class<?> pClz, IPersistencyFilter pNext){
		super(pNext);
		mClass = pClz;
	}
	public boolean doDispatch(IPersistencyEvent pEvent){
		return mClass == pEvent.getLoadClass();
	}
}
