package ch.jester.commonservices.api.preferences;

public interface IPropertyValueChangedListener {

	void propertyValueChanged(String internalKey, Object mValue,
			PreferenceProperty preferenceProperty);

}
