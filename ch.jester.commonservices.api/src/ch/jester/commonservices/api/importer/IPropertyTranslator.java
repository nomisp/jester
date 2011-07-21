package ch.jester.commonservices.api.importer;

public interface IPropertyTranslator {
	public String getProperty(String translatedProperty);
	public String getTranslation(String property);
	public String[] getTranslatedProperties();
}
