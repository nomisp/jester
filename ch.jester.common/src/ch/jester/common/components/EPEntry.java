package ch.jester.common.components;

import ch.jester.commonservices.api.components.IEPEntry;

/**
 * Eine Default Implementation von {@link IEPEntry}
 *
 * @param <T>
 */
public class EPEntry<T> implements IEPEntry<T> {
	private IEPEntry<T> mService;
	@SuppressWarnings("unchecked")
	public EPEntry(T pService){
		mService = (IEPEntry<T>) pService;
	}

	@SuppressWarnings("unchecked")
	@Override
	public T getService() {
		return (T) mService;
	}

	@Override
	public String getProperty(String pPropertyKey) {
		return mService.getProperty(pPropertyKey);
	}
}
