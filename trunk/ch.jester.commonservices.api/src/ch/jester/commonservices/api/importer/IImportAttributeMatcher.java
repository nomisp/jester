package ch.jester.commonservices.api.importer;

import java.util.HashMap;

public interface IImportAttributeMatcher {
	public String[] getInputAttributes();
	public String[] getDomainObjectAttributes();
	public void setInputLinking(HashMap<String, String> pMap);
	public HashMap<String, String> getInputLinking();
	public void resetInputLinking();
	public IPropertyTranslator getPropertyTranslator();
}
