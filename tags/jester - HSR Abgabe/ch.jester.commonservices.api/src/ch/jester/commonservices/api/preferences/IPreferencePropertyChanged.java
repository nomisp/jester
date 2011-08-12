package ch.jester.commonservices.api.preferences;



/**
 * Listener Interface
 *
 */
public interface IPreferencePropertyChanged {

	/**
	 * Wird aufgerufen, wenn sich ein Property geänadert hat
	 * @param internalKey
	 * @param mNewValue
	 * @param preferenceProperty
	 */
	void propertyValueChanged(String internalKey, Object mNewValue,
			IPreferenceProperty preferenceProperty);

}
