package ch.jester.commonservices.api.preferences;

public interface IPreferenceSequencer{
	public IPreferenceProperty create(String pKey, String pLabel,Object value);
	public int size();
	public void writeNext();
	
}
