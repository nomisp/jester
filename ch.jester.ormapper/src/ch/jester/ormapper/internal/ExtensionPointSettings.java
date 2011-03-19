package ch.jester.ormapper.internal;

import java.util.HashMap;

import org.eclipse.core.runtime.IConfigurationElement;

import ch.jester.common.utility.ExtensionPointUtil;

public abstract class ExtensionPointSettings {
	protected IConfigurationElement mElement;
	public ExtensionPointSettings(IConfigurationElement e){
		mElement = e;
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
	protected HashMap<String, String> getAllProperties(String parentName){
		IConfigurationElement element =mElement;
		IConfigurationElement[] children = element.getChildren("Property");
		HashMap<String, String> map = new HashMap<String, String>();
		for(IConfigurationElement child:children){
			map.put(child.getAttribute("name"), child.getAttribute("value")==null?"":child.getAttribute("value"));
		}
		return map;
	}
}
