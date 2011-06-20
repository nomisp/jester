package ch.jester.commonservices.api.preferences;

public interface IPreferenceProperty {

	public abstract String getInternalKey();

	public abstract void setInternalKey(String key);
	
	public abstract String getExternalKey();

	public abstract String getLabel();

	public abstract void setLabel(String label);

	public abstract Object getValue();

	public abstract void setValue(Object value);
	
	public abstract Object getDefaultValue();

	public abstract void setDefaultValue(Object value);

	public abstract Class getType();

	public abstract void setType(Class type);
	
	public IPreferenceManager getManager();

}