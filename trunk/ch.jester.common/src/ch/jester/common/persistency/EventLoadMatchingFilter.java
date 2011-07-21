package ch.jester.common.persistency;

import ch.jester.commonservices.api.persistency.IPersistencyEvent;
import ch.jester.commonservices.api.persistency.IPersistencyFilter;

/**
 * Matcht den Load vom PersistencyEvent auf die übergebene Klasse.
 * Nur dann wird der Event weitergereicht.
 *
 */
public class EventLoadMatchingFilter extends ChainedPersistencyFilter{
	private Class<?> mClass;
	/**
	 Matcht den Load des PersistencyEvent auf die übergebene Klasse.
	 * Nur dann wird der Event weitergereicht.
	 * @param pClz
	 */
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
