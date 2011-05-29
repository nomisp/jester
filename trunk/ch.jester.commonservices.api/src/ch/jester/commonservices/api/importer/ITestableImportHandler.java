package ch.jester.commonservices.api.importer;

import java.io.InputStream;

import org.eclipse.core.runtime.IProgressMonitor;

public interface ITestableImportHandler<T> {
	public Object handleImport(T pInputStream, int pContentLines, IProgressMonitor pMonitor);
}
