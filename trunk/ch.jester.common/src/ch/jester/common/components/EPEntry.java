package ch.jester.common.components;

import ch.jester.commonservices.api.components.IEPEntry;


/**
 * @author  t117221
 */
public class EPEntry<T> implements IEPEntry<T> {
	/**
	 * @uml.property  name="mService"
	 * @uml.associationEnd  
	 */
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
