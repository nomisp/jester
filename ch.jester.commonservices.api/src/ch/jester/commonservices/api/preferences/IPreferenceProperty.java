package ch.jester.commonservices.api.preferences;

public interface IPreferenceProperty {

	public String getInternalKey();

	public void setInternalKey(String key);
	
	public String getExternalKey();

	public String getLabel();

	public void setLabel(String label);

	public Object getValue();

	public void setValue(Object value);
	
	public Object getDefaultValue();

	public void setDefaultValue(Object value);

	public Class<?> getType();

	public void setType(Class<?> type);
	
	public IPreferenceManager getManager();
	
	public IPreferenceProperty setNeedRestartAfterChange(boolean b);
	
	public boolean getNeedRestartAfterChange();
	
	public boolean getEnabled();
	
	public IPreferenceProperty setEnabled(boolean b);

}