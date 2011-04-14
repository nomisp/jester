package ch.jester.common.components;

import ch.jester.commonservices.api.components.IEPEntry;


public class EPEntry<T> implements IEPEntry<T> {
	private IEPEntry<T> mService;
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
