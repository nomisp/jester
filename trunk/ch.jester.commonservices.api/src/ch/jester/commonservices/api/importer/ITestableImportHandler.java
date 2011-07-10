package ch.jester.commonservices.api.importer;

import org.eclipse.core.runtime.IProgressMonitor;

public interface ITestableImportHandler<T> {
	public Object handleImport(T pInputStream, int pContentLines, IProgressMonitor pMonitor);
}
