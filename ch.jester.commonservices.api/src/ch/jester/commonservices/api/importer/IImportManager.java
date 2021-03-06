package ch.jester.commonservices.api.importer;

import java.io.InputStream;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;

import ch.jester.commonservices.api.components.IEPEntryComponentService;




/**Schnittstelle für den Importer
 *
 */
@SuppressWarnings("rawtypes")
public interface IImportManager extends IEPEntryComponentService<IImportHandlerEntry, IImportHandler> {
	/**
	 * Filterdefiniton
	 *
	 */
	interface IImportFilter{
		boolean match(String p);
	}
	/**Importiert die übergebene Resource mit dem spezifizierten IImportHandler, welcher
	 * mit dem Entry assoziiert ist.
	 * @param pEntry der ausgewählte Entry
	 * @param pObjectToImport eine Resource
	 * @return das Resultat
	 */
	public Object doImport(IImportHandlerEntry pEntry, InputStream pObjectToImport, IProgressMonitor pMonitor);
	
	/**Filtert alle regsitrierten IImportHandler aufgrund des Filters
	 * @param pFilter
	 * @return
	 */
	public List<IImportHandlerEntry> filter(IImportFilter pFilter);
	
	/**Erzeugt ein Machting welches sich auf die Extension (z.B *.xy) bezhieht
	 * @param pString z.B. *.xy
	 * @return
	 */
	public IImportFilter createMatchingExtension(String pString);
}
