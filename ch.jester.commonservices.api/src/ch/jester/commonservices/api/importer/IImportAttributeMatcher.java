package ch.jester.commonservices.api.importer;

import java.util.HashMap;

/**
 * @author   t117221
 */
public interface IImportAttributeMatcher {
	public String[] getInputAttributes();
	public String[] getDomainObjectAttributes();
	/**
	 * @param  pMap
	 * @uml.property  name="inputLinking"
	 */
	public void setInputLinking(HashMap<String, String> pMap);
	/**
	 * @return
	 * @uml.property  name="inputLinking"
	 */
	public HashMap<String, String> getInputLinking();
	public void resetInputLinking();
}
