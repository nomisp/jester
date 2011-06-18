package ch.jester.orm;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Set;

import org.eclipse.core.runtime.IContributor;
import org.eclipse.jface.preference.IPreferenceStore;

public class ORMStoreHandler {
	IPreferenceStore mStore;
	IContributor mContributor;
	String prefPrefixKey;
	public ORMStoreHandler(IPreferenceStore pStore, IContributor pContributor){
		mStore = pStore;
		mContributor = pContributor;
		prefPrefixKey = mContributor.getName();
	}
	public HashMap<String, String> getStoredORMConfiguration( Set<String> keys) {
		HashMap<String, String> map = new LinkedHashMap<String, String>();
		Iterator<String> propertyList = keys.iterator(); 
		while(propertyList.hasNext()){
			String key =propertyList.next();
			String value = mStore.getString(prefPrefixKey+"."+key);
			if(value!=null){
				map.put(key, value);
			}
		}
		return map;
	}
	public void setDefaultStoredConfiguration(HashMap<String, String> keys) {
		Iterator<String> propertyList = keys.keySet().iterator(); 
		while(propertyList.hasNext()){
			String key =propertyList.next();
			String value = keys.get(key);
			mStore.setDefault(prefPrefixKey+"."+key, value);
		}
		
	}
}
