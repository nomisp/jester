package ch.jester.commonservices.api.persistencyevent;

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
	public boolean dispatch(PersistencyEvent pEvent) {
		boolean result = doDispatch(pEvent);
		if(result && mNext!=null){
			result = mNext.dispatch(pEvent);
		}
	
		return result;
	}
	
	public boolean doDispatch(PersistencyEvent pEvent){
		return true;
	}
	
}
