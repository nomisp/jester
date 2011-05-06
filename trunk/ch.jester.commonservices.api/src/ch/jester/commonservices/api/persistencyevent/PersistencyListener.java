package ch.jester.commonservices.api.persistencyevent;

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
	public boolean dispatch(PersistencyEvent pEvent) {
		if(mFilter!=null){
			return mFilter.dispatch(pEvent);
		}
		return true;
	}

}
