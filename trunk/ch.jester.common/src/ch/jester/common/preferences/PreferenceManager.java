package ch.jester.common.preferences;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.preferences.IPreferencesService;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.osgi.service.prefs.BackingStoreException;
import org.osgi.service.prefs.Preferences;

import ch.jester.commonservices.api.preferences.IPreferenceManager;
import ch.jester.commonservices.api.preferences.IPreferenceManagerProvider;
import ch.jester.commonservices.api.preferences.IPreferenceProperty;
import ch.jester.commonservices.api.preferences.IPreferenceRegistration;
import ch.jester.commonservices.api.preferences.IPreferencePropertyChanged;
import ch.jester.commonservices.util.ServiceUtility;


public class PreferenceManager implements IPreferenceManager {
	public Set<IPreferenceProperty> mSet = new LinkedHashSet<IPreferenceProperty>();
	private List<IPreferencePropertyChanged> listeners = new ArrayList<IPreferencePropertyChanged>();
	private String mPrefix ="";
	private boolean armed = true;
	private ServiceUtility mServices = new ServiceUtility();
	private boolean mRestart;
	private String mDesc;
	private IPreferencesService service = Platform.getPreferencesService();

	public IPreferenceRegistration getRegistrationService(){
		return mServices.getService(IPreferenceRegistration.class); 
	}
	public void registerProviderAtRegistrationService(IPreferenceManagerProvider prov){
		getRegistrationService().registerPreferenceProvider(prov);
	}
	public void registerProviderAtRegistrationService(String pKey, IPreferenceManagerProvider prov){
		getRegistrationService().registerPreferenceProvider(pKey, prov);
	}

	public IPreferenceManager checkId(String pId){
		return getId().equals(pId)?this:null;
	}
	
	@Override
	public Set<IPreferenceProperty> getProperties(){
		return mSet;
	}
	
	@Override
	public void addProperty(IPreferenceProperty pProperty){
		mSet.add(pProperty);
	}

	@Override
	public IPreferenceProperty getPropertyByInternalKey(String key){
		for(IPreferenceProperty p:mSet){
			if(p.getInternalKey().equals(key)){
				return p;
			}
		}
		return null;
	}

	@Override
	public void setId(String savekey) {
		mPrefix=savekey;
		
	}

	@Override
	public String getId() {
		return mPrefix;
	}

	@Override
	public IPreferenceProperty getPropertyByExternalKey(String key) {
		String internalKey = key.substring(mPrefix.length()+1);
		return getPropertyByInternalKey(internalKey);
	}

	private Preferences getNode(){
		IPreferencesService service = Platform.getPreferencesService();
		Preferences root = service.getRootNode();
		Preferences node = root.node(InstanceScope.SCOPE).node(getId());
		return node;
	}
	private void saveDefault(IPreferenceProperty p){
		Preferences def = getDefaultNode();
		String result = def.get(p.getInternalKey(), null);
		if(result==null){
			def.put(p.getInternalKey(), p.getDefaultValue().toString());
			try {
				//def.sync();
				def.flush();
			} catch (BackingStoreException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	private Preferences getDefaultNode(){
		IPreferencesService service = Platform.getPreferencesService();
		Preferences root = service.getRootNode();
		Preferences node = root.node(InstanceScope.SCOPE).node(getId()+"/default");
		return node;
	}
	
	@Override
	public void propertyValueChanged(IPreferenceProperty preferenceProperty) {
		if(!armed){return;}
		
		Preferences node = getNode();
		
		node.put(preferenceProperty.getInternalKey(), preferenceProperty.getValue().toString());
		try {
			//node.sync();
			node.flush();
		} catch (BackingStoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for(IPreferencePropertyChanged ref:listeners){

				ref.propertyValueChanged(preferenceProperty.getInternalKey(), preferenceProperty.getValue(), preferenceProperty);
		
		}
		
	}

	@Override
	public void addListener(IPreferencePropertyChanged pListener) {
		listeners.add(pListener);
		
	}

	
	@Override
	public IPreferenceProperty create(String pKey, String pLabel, Object value) {
		armed=false;
		IPreferenceProperty prop = new PreferenceProperty(this);
		prop.setLabel(pLabel);
		prop.setInternalKey(pKey);
		prop.setType(value.getClass());
		mSet.add(prop);
		String defaultValue = getDefaultNode().get(pKey, null);
		if(defaultValue==null){
			prop.setDefaultValue(value);
			saveDefault(prop);
		}else{
			prop.setDefaultValue(defaultValue);
		}
		
		String specific = getNode().get(prop.getInternalKey(), null);
		if(specific!=null){
			prop.setValue(specific);
		}else{
			prop.setValue(prop.getDefaultValue());
		}
		armed=true;
		
		return prop;
	}
	@Override
	public void setNeedRestartAfterChange(boolean b) {
		mRestart=b;
		
	}
	@Override
	public boolean getNeedRestartAfterChange() {
		if(!mRestart){
			for(IPreferenceProperty p:mSet){
				if(p.getNeedRestartAfterChange()){
					return true;
				}
			}
		}
		return mRestart;
	}
	@Override
	public void setDescription(String pDesc) {
		mDesc = pDesc;
	}
	@Override
	public String getDescription() {
		return mDesc;
	}
	@Override
	public HashMap<String, String> getPropertiesAsStringMap() {
		HashMap<String, String> map = new HashMap<String, String>();
		for(IPreferenceProperty p:mSet){
			map.put(p.getInternalKey(), p.getValue().toString());
		}
		return map;
	}
	@Override
	public HashMap<String, Object> getPropertiesAsObjectMap() {
		HashMap<String, Object> map = new HashMap<String, Object>();
		for(IPreferenceProperty p:mSet){
			map.put(p.getInternalKey(), p.getValue());
		}
		return map;
	}
}
