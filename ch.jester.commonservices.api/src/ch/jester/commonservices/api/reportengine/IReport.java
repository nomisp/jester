package ch.jester.commonservices.api.reportengine;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.osgi.framework.Bundle;

/**
 * Interface welches ein Report beschreibt. Synonym: Template<br>
 * Um einen Report zu erzeugen, sollte nur {@link IReport#getInstalledFileAsStream()} verwendet werden.
 * Mit diesem kann dann die ReportEngine aufgerufen werden.
 *
 */
public interface IReport {
	/**Setzt den Alias
	 * @param pString
	 */
	public void setAlias(String pString);
	/**
	 * Gibt den Key für interne Zugriffe
	 * @return den Alias
	 */
	public String getAlias();
	/**
	 * Setzt den im UI sichtbaren Namen
	 * @param pString
	 */
	public void setVisibleName(String pString);
	/**
	 * Gibt den sichbaren Namen zurück.
	 * @return
	 */
	public String getVisibleName();
	/**
	 * Gibt den FilenNamen im SourceBundle zurück.
	 * @return
	 */
	public String getBundleFileName();
	/**
	 * Setzt den Namen des Files im SourceBundle
	 * @param pFileName
	 */
	public void setBundleFileName(String pFileName);
	/**
	 * Gibt das File vom Bundle als Stream zurück.
	 * @return
	 * @throws IOException
	 */
	public InputStream getBundleFileAsStream() throws IOException;
	/**
	 * Setzt den externen Pfad, wohin das File geschrieben wurde (Installation)
	 * @param pInstalled
	 */
	public void setInstalledFile(File pInstalled);
	/**
	 * Gibt das aktuelle installierte File zurück
	 * @return
	 */
	public File getInstalledFile();
	/**
	 * Gibt das installierte File als Stream.
	 * 
	 * @return
	 * @throws IOException
	 */
	public InputStream getInstalledFileAsStream() throws IOException;
	/**
	 * Setzt das SourceBundle
	 * @param b
	 */
	public void setBundle(Bundle b);
}
