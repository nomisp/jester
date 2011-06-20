package ch.jester.commonservices.api.preferences;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.preferences.IPreferencesService;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.osgi.service.prefs.BackingStoreException;
import org.osgi.service.prefs.Preferences;

public class PreferenceManager implements IPreferenceManager {
	public Set<IPreferenceProperty> mSet = new LinkedHashSet<IPreferenceProperty>();
	private List<IPropertyValueChangedListener> listeners = new ArrayList<IPropertyValueChangedListener>();
	private String mPrefix ="";
	private boolean armed = true;
	private IPreferencesService service = Platform.getPreferencesService();

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
	public void setPrefixKey(String savekey) {
		mPrefix=savekey;
		
	}

	@Override
	public String getPrefixKey() {
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
		Preferences node = root.node(InstanceScope.SCOPE).node(getPrefixKey());
		return node;
	}
	private void saveDefault(IPreferenceProperty p){
		Preferences def = getDefaultNode();
		String result = def.get(p.getInternalKey(), null);
		if(result==null){
			def.put(p.getInternalKey(), p.getDefaultValue().toString());
			try {
				def.sync();
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
		Preferences node = root.node(InstanceScope.SCOPE).node(getPrefixKey()+"/default");
		return node;
	}
	
	@Override
	public void propertyValueChanged(PreferenceProperty preferenceProperty) {
		if(!armed){return;}
		
		Preferences node = getNode();
		
		node.put(preferenceProperty.getInternalKey(), preferenceProperty.getValue().toString());
		try {
			node.sync();
		} catch (BackingStoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for(IPropertyValueChangedListener l:listeners){
			l.propertyValueChanged(preferenceProperty.getInternalKey(), preferenceProperty.mValue, preferenceProperty);
		}
		
	}

	@Override
	public void addListener(IPropertyValueChangedListener pListener) {
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
}
