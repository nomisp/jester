package ch.jester.commonservices.filter;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;

public interface IFilter {
	
	public IStatus filter(String pSearch, IProgressMonitor pMonitor);
}
