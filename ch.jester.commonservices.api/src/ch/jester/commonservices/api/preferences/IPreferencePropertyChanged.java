package ch.jester.commonservices.api.preferences;



/**
 * Listener Interface
 *
 */
public interface IPreferencePropertyChanged {

	void propertyValueChanged(String internalKey, Object mNewValue,
			IPreferenceProperty preferenceProperty);

}
