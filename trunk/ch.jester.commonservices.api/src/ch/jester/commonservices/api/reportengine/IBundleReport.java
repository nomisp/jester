package ch.jester.commonservices.api.reportengine;

import java.io.IOException;
import java.io.InputStream;

import org.osgi.framework.Bundle;

/**
 * Ein in einem Bundle installierter Report.
 */
public interface IBundleReport extends IReport {
	/**
	 * Das Sourceverzeichnis für Reports
	 * 
	 * @param pRoot
	 */
	public void setBundleSourceRoot(String pRoot);

	/**
	 * Gibt das Sourceverzeichnis zurück
	 * 
	 * @return
	 */
	public String getBundleSourceRoot();

	/**
	 * Gibt den FilenNamen im SourceBundle zurück.
	 * 
	 * @return
	 */
	public String getBundleReportFile();

	/**
	 * Setzt den Namen des Files im SourceBundle
	 * 
	 * @param pFileName
	 */
	public void setBundleReportFile(String pFileName);

	/**
	 * Gibt das File vom Bundle als Stream zurück.
	 * 
	 * @return
	 * @throws IOException
	 */
	public InputStream getBundleFileAsStream() throws IOException;

	/**
	 * Setzt das SourceBundle
	 * 
	 * @param b
	 */
	public void setBundle(Bundle b);

	/**
	 * Das Bundle in dem sich der Report befindet.
	 * 
	 * @return
	 */
	public Bundle getBundle();
}
