package ch.jester.commonservices.api.importer;

import java.io.IOException;
import java.util.List;

import ch.jester.commonservices.api.preferences.IPreferenceManagerProvider;

/**
 * Ermöglicht den IImportHandler den Zugriff auf Ressourcen, sprich Links,<br>
 * welche auf einer Website liegen.<br>
 * 
 */
public interface IWebImportAdapter extends IImportHandler<Object>, IPreferenceManagerProvider {
	/**
	 * @return
	 * @throws IOException
	 */
	public void setIImportHandler(IImportHandler<Object> pAdaptedHandler);
	
	public List<ILink> getLinks()  throws IOException ;
	
	public void setDownloadAddress(String pAddress);
	
} 
