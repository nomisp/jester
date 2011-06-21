package ch.jester.commonservices.api.preferences;

/**
 * Ein Property, welches im UI dargestellt werden kann.
 *
 */
public interface IPreferenceProperty {


	/**Den internen Key;
	 * siehe auch {@link IPreferenceProperty#setInternalKey(String)}
	 * @return
	 */
	public String getInternalKey();
	/**Ein einfacher im {@link IPreferenceManager} eindeutiger key<br>
	 * z.b debug
	 * @return
	 */
	public void setInternalKey(String key);
	
	/**Den Externen key:<br>
	 * {@link IPreferenceManager#getId()}+"."+{@link IPreferenceProperty#getInternalKey()}
	 * @return
	 */
	public String getExternalKey();

	/**
	 * Das Label im Ui
	 * @return
	 */
	public String getLabel();

	/**Setzt das Label fürs UI
	 * @param label
	 */
	public void setLabel(String label);

	/**
	 * @return den Wert des Properties oder der {@link #getDefaultValue()}
	 */
	public Object getValue();

	/**
	 * Setzt den Wert des Properties
	 * @param value
	 */
	public void setValue(Object value);
	
	/**
	 * 
	 * @return Den DefaultWert
	 */
	public Object getDefaultValue();

	/**
	 * Setzt den default Wert
	 * @param value
	 */
	public void setDefaultValue(Object value);

	/**
	 * Die Klase des Properties
	 * @return
	 */
	public Class<?> getType();

	/**
	 * Setzt die Klasse des Properties
	 * @param type
	 */
	public void setType(Class<?> type);
	
	/**
	 * Gibt den Manager zurück, welches das Property erzeugt hat
	 * @return
	 */
	public IPreferenceManager getManager();
	
	/**
	 * Setzt ob das System einen Restart benötigt, falls das Propery geändert wurde
	 * @param b
	 * @return
	 */
	public IPreferenceProperty setNeedRestartAfterChange(boolean b);
	
	/**Sagt ob das System einen Restart benötigt, falls das Propery geändert wurde
	 * @return
	 */
	public boolean getNeedRestartAfterChange();
	
	/**
	 * Ist das Property editierbar?
	 * @return
	 */
	public boolean getEnabled();
	
	/**
	 * Soll das Property editierbar sein
	 * @param b
	 * @return
	 */
	public IPreferenceProperty setEnabled(boolean b);

	/**
	 * Setzt auswählbare Werte
	 * @param names
	 */
	public void setSelectableValues(String[][] names);
	
	/**
	 * @return gibt die auswählbaren Werte oder null
	 */
	public String[][] getSelectableValues();

}