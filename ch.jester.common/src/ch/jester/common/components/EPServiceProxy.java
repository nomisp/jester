package ch.jester.common.components;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import org.eclipse.core.runtime.IConfigurationElement;

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
	public EPServiceProxy(IConfigurationElement pConfigurationElement,
			Class<T> pClassType, String pExecutableAttribute) {
		mElement=pConfigurationElement;
		mClassType=pClassType;
		mExec = pExecutableAttribute;
	}

	private String getProperty(String pPropertyKey) {
		return mElement.getAttribute(pPropertyKey);
	}
	
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
		if(mService!=null){
			return arg1.invoke(arg0, arg2);
		}
		
		if(arg1.getName().equals("toString")){
			return toString();
		}else if(arg1.getName().equals("getProperty")){
			return getProperty(arg2[0].toString());
		}
		mService = mElement.createExecutableExtension(mExec);
		return invoke(mService, arg1, arg2);
	}
}
