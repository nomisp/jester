package ch.jester.commonservices.api.importer;

import java.io.InputStream;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;

import ch.jester.commonservices.api.IComponentService;




/**Schnittstelle für den Importer
 *
 */
public interface IImportManager extends IComponentService<IImportHandler> {
	interface IImportFilter{
		boolean match(String p);
	}
	/**Gibt die registrierten IImportHandler in Form von Entries zurück.<br>
	 * <b>Achtung:</b> Je nach Implementation kann sich die Liste dynamisch ändern.
	 * @return Liste von IImportHandllerEntry
	 */
	public List<IImportHandlerEntry> getRegistredImportHandlers();
	/**Importiert die übergebene Resource mit dem spezifizierten IImportHandler, welcher
	 * mit dem Entry assoziiert ist.
	 * @param pEntry der ausgewählte Entry
	 * @param pObjectToImport eine Resource
	 * @return das Resultat
	 */
	public Object doImport(IImportHandlerEntry pEntry, InputStream pObjectToImport, IProgressMonitor pMonitor);
	public List<IImportHandlerEntry> filter(IImportFilter pFilter);
	
	public IImportFilter createMatchingExtension(String pString);
}
