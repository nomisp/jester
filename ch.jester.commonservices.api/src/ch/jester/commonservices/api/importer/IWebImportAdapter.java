package ch.jester.commonservices.api.importer;

import java.io.IOException;
import java.util.List;

/**
 * Ermöglicht den IImportHandler den Zugriff auf Ressourcen, sprich Links,<br>
 * welche auf einer Website liegen.<br>
 * 
 */
public interface IWebImportAdapter extends IImportHandler<Object> {
	/**
	 * @return
	 * @throws IOException
	 */
	public void setIImportHandler(IImportHandler pAdaptedHandler);
	
	public List<ILink> getLinks()  throws IOException ;
} 
