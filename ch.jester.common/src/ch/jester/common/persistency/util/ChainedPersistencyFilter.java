package ch.jester.common.persistency.util;

import ch.jester.commonservices.api.persistency.IPersistencyEvent;
import ch.jester.commonservices.api.persistency.IPersistencyFilter;

public abstract class ChainedPersistencyFilter implements IPersistencyFilter{
	protected IPersistencyFilter mNext;
	public ChainedPersistencyFilter(IPersistencyFilter pFilter){
		mNext = pFilter;
	}
	public void setNext(IPersistencyFilter pFilter){
		mNext = pFilter;
	}
	
	public ChainedPersistencyFilter(){
	}
	@Override
	public boolean dispatch(IPersistencyEvent pEvent) {
		boolean result = doDispatch(pEvent);
		if(result && mNext!=null){
			result = mNext.dispatch(pEvent);
		}
	
		return result;
	}
	
	public boolean doDispatch(IPersistencyEvent pEvent){
		return true;
	}
	
}