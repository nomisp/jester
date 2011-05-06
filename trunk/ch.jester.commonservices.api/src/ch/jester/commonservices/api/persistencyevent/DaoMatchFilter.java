package ch.jester.commonservices.api.persistencyevent;

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
		public boolean doDispatch(PersistencyEvent pEvent){
			return !(mDao==pEvent.getSource());
		}
	
}
