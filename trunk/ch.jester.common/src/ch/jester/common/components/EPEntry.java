package ch.jester.common.components;

import ch.jester.commonservices.api.components.IEPEntry;
import ch.jester.commonservices.api.components.IEPService;


public class EPEntry<T> implements IEPEntry<T> , IEPService{
	private IEPService mService;
	public EPEntry(T pService){
		mService = (IEPService) pService;
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
