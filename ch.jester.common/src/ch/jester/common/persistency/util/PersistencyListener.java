package ch.jester.common.persistency.util;

import ch.jester.commonservices.api.persistency.IPersistencyEvent;
import ch.jester.commonservices.api.persistency.IPersistencyFilter;
import ch.jester.commonservices.api.persistency.IPersistencyListener;

public abstract class PersistencyListener implements IPersistencyListener{
	private IPersistencyFilter mFilter;
	
	public PersistencyListener(){
		
	}
	public PersistencyListener(IPersistencyFilter pFilter){
		mFilter = pFilter;
	}
	
	@Override
	public void setFilter(IPersistencyFilter pFilter) {
		mFilter = pFilter;
	}

	@Override
	public boolean dispatch(IPersistencyEvent pEvent) {
		if(mFilter!=null){
			return mFilter.dispatch(pEvent);
		}
		return true;
	}

}
