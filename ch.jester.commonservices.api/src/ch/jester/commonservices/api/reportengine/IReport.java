package ch.jester.commonservices.api.reportengine;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * Interface welches ein Report beschreibt. Synonym: Template<br> Mit diesem kann die ReportEngine aufgerufen werden.
 */
public interface IReport {

	/**
	 * Setzt den Alias (interner Zugriff)
	 * @param  pString
	 * @uml.property  name="alias"
	 */
	public abstract void setAlias(String pString);

	/**
	 * Gibt den Key für interne Zugriffe
	 * @return   den Alias
	 * @uml.property  name="alias"
	 */
	public abstract String getAlias();

	/**
	 * Setzt den im UI sichtbaren Namen
	 * @param  pString
	 * @uml.property  name="visibleName"
	 */
	public abstract void setVisibleName(String pString);

	/**
	 * Gibt den sichbaren Namen zurück.
	 * @return
	 * @uml.property  name="visibleName"
	 */
	public abstract String getVisibleName();

	/**
	 * Setzt den externen Pfad, wohin das File geschrieben wurde (Installation)
	 * @param  pInstalled
	 * @uml.property  name="installedFile"
	 */
	public abstract void setInstalledFile(File pInstalled);

	/**
	 * Gibt das aktuelle installierte File zurück
	 * @return
	 * @uml.property  name="installedFile"
	 */
	public abstract File getInstalledFile();

	/**
	 * Gibt das installierte File als Stream.
	 * 
	 * @return
	 * @throws IOException
	 */
	public abstract InputStream getInstalledFileAsStream() throws IOException;
	
	/**
	 * Setzt die Klasse, welcher der Report als Input erwartet
	 * @param pClass
	 */
	public void setInputBeanClass(Class<?> pClass);

	
	/**
	 * @return Die erwartete Input Klasse
	 */
	public Class<?> getInputBeanClass();

}