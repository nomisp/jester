package ch.jester.common.ui.filter;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.ui.IViewPart;

/**
 * Setzt einen Filter, damit der Content der View entsprechend kleiner wird.
 *
 */
public interface IUIFilter {
	
	/**
	 * filtern
	 * @param pSearch danach wird gesucht.
	 * @param pPart im Part
	 * @param pMonitor für async 
	 * @return einen Status ob OK, NOK.
	 */
	public IStatus filter(String pSearch, IViewPart pPart,  IProgressMonitor pMonitor);
	
	/**
	 * reset des Suchens
	 */
	public void clear();
}
