package ch.jester.commonservices.api.preferences;

import java.util.Set;

public interface IPreferenceManager {

	public abstract Set<IPreferenceProperty> getProperties();

	public abstract void addProperty(IPreferenceProperty pProperty);

	public abstract IPreferenceProperty create(String pKey, String pLabel,
			Object value);

	public abstract IPreferenceProperty getPropertyByInternalKey(String key);
	public abstract IPreferenceProperty getPropertyByExternalKey(String key);
	public void setPrefixKey(String savekey);
	public String getPrefixKey();

	public abstract void propertyValueChanged(
			IPreferenceProperty preferenceProperty);

	public abstract void addListener(IPreferencePropertyChanged abstractWebAdapter);

	public void setNeedRestartAfterChange(boolean b);
	
	public boolean getNeedRestartAfterChange();
}