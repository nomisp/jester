package ch.jester.common.utility;

import java.util.HashMap;

import org.eclipse.core.runtime.IConfigurationElement;


/**
 * Utility Klasse
 * Damit kann auf einfache Weise die Attribute des ExtensionPoints ausgelesen werden
 */
public abstract class ExtensionPointSettings {
	protected IConfigurationElement mElement;
	public ExtensionPointSettings(IConfigurationElement e){
		mElement = e;
	}
	public void setConfigurationElement(IConfigurationElement e){
		mElement = e;
	}
	
	public ExtensionPointSettings(String pBundleName, String pExtensionPoint){
		 mElement = ExtensionPointUtil.getExtensionPointElement(pBundleName, pExtensionPoint);
	}
	
	protected String getExtensionPointValueFromElement(String string) {
		return mElement.getAttribute(string);
	}

	
	protected String getExtensionpointvalue(IConfigurationElement element, String attributename ){
		String value = element.getAttribute(attributename);
		return value;
	}

	protected String getProperties(String elementname,String attributename ){
		IConfigurationElement element = mElement;
		IConfigurationElement[] children = element.getChildren("Property");
	
		for(IConfigurationElement child:children){
			if(child.getAttribute("name").equals(attributename)){
				return child.getAttribute("value")==null?"":child.getAttribute("value");
			}
		}
		return null;
	}
	protected HashMap<String, String> getAllProperties(String pChildName){
		IConfigurationElement element =mElement;
		IConfigurationElement[] children = element.getChildren(pChildName);
		HashMap<String, String> map = new HashMap<String, String>();
		for(IConfigurationElement child:children){
			map.put(child.getAttribute("name"), child.getAttribute("value")==null?"":child.getAttribute("value"));
		}
		return map;
	}
}
