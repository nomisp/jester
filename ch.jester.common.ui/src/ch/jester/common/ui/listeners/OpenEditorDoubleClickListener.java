package ch.jester.common.ui.listeners;

import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;

import ch.jester.common.ui.services.IEditorService;
import ch.jester.common.ui.utility.SelectionUtility;
import ch.jester.commonservices.util.ServiceUtility;

/**
 * Default DoppelKlick Listener, der allenfalls einen Editor öffnet
 * 
 *
 */
public class OpenEditorDoubleClickListener implements IDoubleClickListener{
	private ServiceUtility mServices = new ServiceUtility();
	@Override
	public void doubleClick(DoubleClickEvent event) {
			Object selectedObject = new SelectionUtility(event.getSelection()).getFirstSelected();
			mServices.getService(IEditorService.class).openEditor(selectedObject);
	}

}
