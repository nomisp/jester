package ch.jester.common.ui.services;

import org.eclipse.jface.action.IStatusLineManager;

/**
 * Die setMessage Methoden sind threadsafe zu implementieren, d.h. die Implementation muss
 * dafür sorgen, dass die setMessage Methoden immer auf dem UIThread ausgeführt
 * werden.
 *
 */
public interface IExtendedStatusLineManager extends IStatusLineManager{
	/**
	 * Nach der angegeben Zeit wird die Message gelöscht.
	 * Falls es Messages gibt, die auf noch nicht disposed sind, so wird
	 * die aktuelle Message überschrieben und die dispose Zeit zurückgesetzt
	 * @param pMessage
	 * @param pDisposesInMilis
	 */
	public void setMessage(String pMessage, int pDisposesInMilis);
}
