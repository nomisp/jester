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
	 * @param  pRoot
	 * @uml.property  name="bundleSourceRoot"
	 */
	public void setBundleSourceRoot(String pRoot);

	/**
	 * Gibt das Sourceverzeichnis zurück
	 * @return
	 * @uml.property  name="bundleSourceRoot"
	 */
	public String getBundleSourceRoot();

	/**
	 * Gibt den FilenNamen im SourceBundle zurück.
	 * @return
	 * @uml.property  name="bundleReportFile"
	 */
	public String getBundleReportFile();

	/**
	 * Setzt den Namen des Files im SourceBundle
	 * @param  pFileName
	 * @uml.property  name="bundleReportFile"
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
	 * @param  b
	 * @uml.property  name="bundle"
	 */
	public void setBundle(Bundle b);

	/**
	 * Das Bundle in dem sich der Report befindet.
	 * @return
	 * @uml.property  name="bundle"
	 */
	public Bundle getBundle();
}
