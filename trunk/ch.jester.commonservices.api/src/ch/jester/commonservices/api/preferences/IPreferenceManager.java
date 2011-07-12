package ch.jester.commonservices.api.preferences;

import java.util.HashMap;
import java.util.Set;



/**
 * Interface für ein PreferenceManager
 *
 */
public interface IPreferenceManager {

	public Set<IPreferenceProperty> getProperties();

	//public void addProperty(IPreferenceProperty pProperty);

	/**
	 * Erzeugt ein neues {@link IPreferenceProperty}
	 * @param pKey der interne Key
	 * @param pLabel das Label fürs UI
	 * @param value der DefaultValue
	 * @return
	 */
	public IPreferenceProperty create(String pKey, String pLabel,Object value);

	/**Sucht ein Property gemäss internem key
	 * @param key
	 * @return aProperty oder null
	 */
	public IPreferenceProperty getPropertyByInternalKey(String key);
	/**Sucht ein Property gemäss externem key
	 * @param key
	 * @return aProperty oder null
	 */
	public IPreferenceProperty getPropertyByExternalKey(String key);
	
	/**
	 * Die ID des Managers
	 * @param savekey
	 */
	public void setId(String savekey);
	
	/**
	 * Die ID des Managers
	 * @return
	 */
	public String getId();

	/**
	 * @param preferenceProperty
	 */
	public  void propertyValueChanged(IPreferenceProperty preferenceProperty);

	/**
	 * Ein Listener hinzufügen
	 * @param pListener
	 */
	public void addListener(IPreferencePropertyChanged pListener);

	/**
	 * Globales setzten für einen restart.
	 * Die {@link IPreferenceProperty#getNeedRestartAfterChange()} werden ignoriert <code>b==true<code>
	 * @param b
	 */
	public void setNeedRestartAfterChange(boolean b);
	
	/**Restart nötig?
	 * @return
	 */
	public boolean getNeedRestartAfterChange();
	
	/**
	 * Ein Beschreibung oder null.
	 * @param pDesc
	 */
	public void setDescription(String pDesc);
	
	/**
	 * Die Beschreibung
	 * @return
	 */
	public String getDescription();
	
	/**gibt falls die Id matcht, den Manager zurück
	 * @param pId
	 * @return
	 */
	public IPreferenceManager checkId(String pId);
	
	/**Registriert den PreferencemanagerProvider
	 * @param prov
	 */
	public void registerProviderAtRegistrationService(IPreferenceManagerProvider prov);
	/**Registriert den PreferencemanagerProvider mit der Id
	 * @param pKey
	 * @param prov
	 */
	public void registerProviderAtRegistrationService(String pKey, IPreferenceManagerProvider prov);
	
	/**
	 * @return
	 */
	public HashMap<String, String> getPropertiesAsStringMap();
	/**
	 * @return
	 */
	public HashMap<String, Object> getPropertiesAsObjectMap();
}