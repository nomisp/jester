package ch.jester.common.persistency.util;

import ch.jester.commonservices.api.persistency.IPersistencyEvent;
import ch.jester.commonservices.api.persistency.IPersistencyFilter;

public class DaoMatchFilter  extends ChainedPersistencyFilter{
		private Object mDao;
		public DaoMatchFilter(Object persister){
			super();
			mDao = persister;
		}
		public DaoMatchFilter(Object persister, IPersistencyFilter pNext){
			super(pNext);
			mDao = persister;
		}
		public boolean doDispatch(IPersistencyEvent pEvent){
			return !(mDao==pEvent.getSource());
		}
	
}
