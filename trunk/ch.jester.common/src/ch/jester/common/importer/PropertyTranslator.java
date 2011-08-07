package ch.jester.common.importer;

import java.util.ArrayList;
import java.util.List;

import ch.jester.commonservices.api.importer.IPropertyTranslator;

/**
 * DefaultImpl. von IPropertyTranslator.
 *
 */
public class PropertyTranslator implements IPropertyTranslator{
	private List<TranslatedProperty> tp = new ArrayList<TranslatedProperty>();
	@Override
	public String getProperty(String translatedProperty) {
		for(TranslatedProperty prop:tp){
			if(prop.label.equals(translatedProperty)){
				return prop.property;
			}
		}
		return null;
	}

	@Override
	public String getTranslation(String property) {
		for(TranslatedProperty prop:tp){
			if(prop.property.equals(property)){
				return prop.label;
			}
		}
		return null;
	}

	public void create(String property, String label){
		TranslatedProperty tprop = new TranslatedProperty();
		tprop.property=property;
		tprop.label=label;
		tp.add(tprop);
	}
	
	
	
	class TranslatedProperty{
		public String label;
		public String property;
	}



	@Override
	public String[] getTranslatedProperties() {
		List<String> trans = new ArrayList<String>();
		for(TranslatedProperty t:tp){
			trans.add(t.label);
		}
		return trans.toArray(new String[trans.size()]);
	}
}
