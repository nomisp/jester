package ch.jester.common.components;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import org.eclipse.core.runtime.IConfigurationElement;

import ch.jester.commonservices.api.components.IEPEntry;
import ch.jester.commonservices.api.components.IEPEntryConfig;

/**
 * Simple ProxyKlasse für Services.
 * Sollte nicht von Klients explizit verwendet werden.
 * @param <T>
 */
class EPServiceProxy<T> implements InvocationHandler{
	private IConfigurationElement mElement;
	private Class<T> mClassType;
	private String mExec;
	private Object mService;
	private Object mEntry;
	public EPServiceProxy(IConfigurationElement pConfigurationElement,
			Class<T> pClassType, String pExecutableAttribute) {
		mElement=pConfigurationElement;
		mClassType=pClassType;
		mExec = pExecutableAttribute;
	}

	private String getProperty(String pPropertyKey) {
		return mElement.getAttribute(pPropertyKey);
	}
	
	/** Zusammen-Stringen der Attribute
	 * @return
	 */
	private String getAttributes(){
		StringBuilder sbuilder = new StringBuilder();
		String[] names = mElement.getAttributeNames();
		for(String name:names){
			sbuilder.append(name);
			sbuilder.append("=");
			sbuilder.append(mElement.getAttribute(name));
			sbuilder.append("; ");
		}
		return sbuilder.toString();
	}

	public String toString(){
		return "Proxy ("+mClassType+" - "+getAttributes()+" )";
	}
	
	@Override
	public Object invoke(Object arg0, Method arg1, Object[] arg2)
			throws Throwable {
		
		if(arg1.getName().equals("toString")){
			return toString();
		}else if(arg1.getName().equals("getProperty")){
			return getProperty(arg2[0].toString());
		}else if(arg1.getName().equals("setEPEntry")){
			mEntry = arg2[0];
			return null;
		}
		if(mService!=null){
			return arg1.invoke(mService, arg2);
		}
		mService = mElement.createExecutableExtension(mExec);
		if(mEntry!=null){
			((IEPEntryConfig)mService).setEPEntry((IEPEntry<?>) mEntry);
		}
		return invoke(mService, arg1, arg2);
	}
}
