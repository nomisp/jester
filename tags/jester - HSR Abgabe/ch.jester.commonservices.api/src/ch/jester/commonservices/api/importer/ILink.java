package ch.jester.commonservices.api.importer;

import java.io.IOException;

/**
 * Das Interface beschreibt einen ausführbaren (herunterladbaren) Link<b>
 * www.acme.com/downloads/1.zip
 *
 */
public interface ILink {

	/**den Text auf den sich <a href/>
	 * @return
	 */
	public String getText();

	/**Die <a href/>
	 * @return
	 */
	public String getURL();

	/**Falls <a href/> relativ ist, kann die Root Addresse gesetzt werden.
	 * @param pAddress
	 */
	public void setRoot(String pAddress);

	/**Versucht den Link in das angegebene Verzeichnis herunterzuladen
	 * @param pTargetFile
	 * @throws IOException
	 */
	public void download(String pTargetFile) throws IOException;

}