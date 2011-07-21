package ch.jester.commonservices.api.importer;

import org.eclipse.core.runtime.IProgressMonitor;

import ch.jester.commonservices.exceptions.ProcessingException;

/**
 * Ein TestableImportHandler importiert typischer Weise keine Daten, sondern testet nur, ob es funktionieren würde
 *
 * @param <T>
 */
public interface ITestableImportHandler<T> {
	/**
	 * Test ob ein Import funktioniert.
	 * Konvention: Wird keine Exception geworfen, so klappt es.
	 * 
	 * @param pInputStream
	 * @param pContentLines
	 * @param pMonitor
	 * @return
	 */
	public Object handleImport(T pInputStream, int pContentLines, IProgressMonitor pMonitor) throws ProcessingException;
}
