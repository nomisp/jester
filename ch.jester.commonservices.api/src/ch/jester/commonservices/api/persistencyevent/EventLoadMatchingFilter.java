package ch.jester.commonservices.api.persistencyevent;

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
	public boolean doDispatch(PersistencyEvent pEvent){
		return mClass == pEvent.getLoadClass();
	}
}
