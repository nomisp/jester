package ch.jester.commonservices.api.preferences;

import java.util.Set;



public interface IPreferenceManager {

	public Set<IPreferenceProperty> getProperties();

	public void addProperty(IPreferenceProperty pProperty);

	public IPreferenceProperty create(String pKey, String pLabel,Object value);

	public IPreferenceProperty getPropertyByInternalKey(String key);
	public IPreferenceProperty getPropertyByExternalKey(String key);
	public void setPrefixKey(String savekey);
	public String getPrefixKey();

	public  void propertyValueChanged(
			IPreferenceProperty preferenceProperty);

	public void addListener(IPreferencePropertyChanged abstractWebAdapter);

	public void setNeedRestartAfterChange(boolean b);
	
	public boolean getNeedRestartAfterChange();
	
	public void setDescription(String pDesc);
	
	public String getDescription();
}