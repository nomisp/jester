package ch.jester.commonservices.api.preferences;


public interface IPreferenceRegistration {
	
	public void registerPreferenceProvider(String pId, IPreferenceManagerProvider pProvider);
	public void registerPreferenceProvider(IPreferenceManagerProvider pProvider);
	public void unregisterPreferenceProvider(String pId);
	public void unregisterPreferenceProvider(IPreferenceManagerProvider pProvider);

	public IPreferenceManagerProvider findProvider(String pId);
}
