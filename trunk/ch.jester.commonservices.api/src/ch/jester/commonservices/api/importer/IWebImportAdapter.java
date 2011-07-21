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
	 * Der zu adaptierende Handler
	 * @param pAdaptedHandler
	 */
	public void setIImportHandler(IImportHandler<Object> pAdaptedHandler);
	
	/**
	 * Gibt die Links zurück, welche verfügbar sind
	 * @return
	 * @throws IOException
	 */
	public List<ILink> getLinks()  throws IOException ;
	
	/**
	 * Setzt eine Downloadadresse (Root Adresse)
	 * @param pAddress
	 */
	public void setDownloadAddress(String pAddress);
	
} 
