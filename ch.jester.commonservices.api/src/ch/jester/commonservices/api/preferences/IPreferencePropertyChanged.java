package ch.jester.commonservices.api.preferences;



public interface IPreferencePropertyChanged {

	void propertyValueChanged(String internalKey, Object mValue,
			IPreferenceProperty preferenceProperty);

}
