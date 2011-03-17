package ch.jester.ormapper.util;

import java.util.HashMap;

import org.eclipse.core.runtime.IConfigurationElement;

import ch.jester.common.utility.ExtensionPointUtil;
import ch.jester.ormapper.api.IORMapperSettings;
import ch.jester.ormapper.internal.ORMapperActivator;

public class AbstractORMapperExtensionPointSettings implements IORMapperSettings{
	@Override
	public  String getConnectiondriverclass() {
		return getExtensionpointvalue("Configuration","JDBCDriverClass");		
	}

	/**
	 * liefert den Namen der Class des SQL-dialektes
	 * liest diesen aus der Extension des Plugins aus
	 * @return
	 */
	@Override
	public  String getSqldialect() {
		return getExtensionpointvalue("Configuration","SQLDialectClass");		
	}
	/**
	 * liefert den Namen des Subprotocols 
	 * dieser wird als Bestandteil der Verbindungs-URL verwendet 
	 * liest diesen aus der Extension des Plugins aus
	 * @return
	 */
	@Override
	public  String getSubprotocol() {
		return getExtensionpointvalue("Configuration","Subprotocol");		
	}

	
	private String getExtensionpointvalue(String elementname,String attributename ){
		IConfigurationElement element = ExtensionPointUtil.getExtensionPointElement(ORMapperActivator.getDefault().getActivationContext().getPluginId(), elementname);
		String value = element.getAttribute(attributename);
		return value;
	}
	private String getProperties(String elementname,String attributename ){
		IConfigurationElement element = ExtensionPointUtil.getExtensionPointElement(ORMapperActivator.getDefault().getActivationContext().getPluginId(), elementname);
		IConfigurationElement[] children = element.getChildren("Property");
	
		for(IConfigurationElement child:children){
			if(child.getAttribute("name").equals(attributename)){
				return child.getAttribute("value")==null?"":child.getAttribute("value");
			}
		}
		return null;
	}
	private HashMap<String, String> getAllProperties(String parentName){
		IConfigurationElement element = ExtensionPointUtil.getExtensionPointElement(ORMapperActivator.getDefault().getActivationContext().getPluginId(), parentName);
		IConfigurationElement[] children = element.getChildren("Property");
		HashMap<String, String> map = new HashMap<String, String>();
		for(IConfigurationElement child:children){
			map.put(child.getAttribute("name"), child.getAttribute("value")==null?"":child.getAttribute("value"));
		}
		return map;
	}
	@Override
	public HashMap<String, String> getConfiguration() {
		return getAllProperties("Configuration");
	}
	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public String getUser() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public String getConnectionurl() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public String getDbname() {
		// TODO Auto-generated method stub
		return null;
	}

}
