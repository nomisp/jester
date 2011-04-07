package ch.jester.common.ui.services;

import org.eclipse.jface.action.IStatusLineManager;

public interface IExtendedStatusLineManager extends IStatusLineManager{
	public void setMessage(String pMessage, int pDisposesInMilis);
}
