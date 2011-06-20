package ch.jester.commonservices.api.preferences;

public interface IPreferenceManagerProvider {
	public IPreferenceManager getPreferenceManager(String pKey);
}
