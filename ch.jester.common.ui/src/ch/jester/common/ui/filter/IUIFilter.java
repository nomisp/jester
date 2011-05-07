package ch.jester.common.ui.filter;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.ui.IViewPart;

public interface IUIFilter {
	
	public IStatus filter(String pSearch, IViewPart pPart,  IProgressMonitor pMonitor);
}
