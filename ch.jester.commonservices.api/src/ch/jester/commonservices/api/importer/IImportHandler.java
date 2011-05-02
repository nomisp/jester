package ch.jester.commonservices.api.importer;

import java.io.InputStream;

import org.eclipse.core.runtime.IProgressMonitor;

/**
 * Importiert eine Source
 *
 */
public interface IImportHandler<T> {

	/**Importiert den Stream
	 * @param pInputStream der Stream
	 * @return ein Object
	 */
	public Object handleImport(InputStream pInputStream, IProgressMonitor pMonitor);
}
