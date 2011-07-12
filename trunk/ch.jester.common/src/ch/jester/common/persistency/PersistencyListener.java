package ch.jester.common.persistency;

import ch.jester.commonservices.api.persistency.IPersistencyEvent;
import ch.jester.commonservices.api.persistency.IPersistencyFilter;
import ch.jester.commonservices.api.persistency.IPersistencyListener;

/**
 * Ein Listener mit der Möglichkeit Filter zu verketten.
 */
public abstract class PersistencyListener implements IPersistencyListener{
	/**
	 * @uml.property  name="mFilter"
	 * @uml.associationEnd  
	 */
	private IPersistencyFilter mFilter;
	
	public PersistencyListener(){
		
	}
	/**
	 * Installiert den Filter
	 * @param pFilter
	 */
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
