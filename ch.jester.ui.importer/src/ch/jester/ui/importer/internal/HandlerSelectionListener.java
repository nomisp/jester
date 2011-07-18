package ch.jester.ui.importer.internal;

import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;

import ch.jester.common.ui.utility.SelectionUtility;
import ch.jester.commonservices.api.importer.IImportHandlerEntry;

public class HandlerSelectionListener implements ISelectionChangedListener {
	SelectionUtility su = new SelectionUtility(null);
	private Controller mContoller;
	public HandlerSelectionListener(Controller pController) {
		mContoller=pController;
	}
	@Override
	public void selectionChanged(SelectionChangedEvent event) {
		su.setSelection(event.getSelection());
		mContoller.setSelectedHandlerEntry(su.getFirstSelectedAs(IImportHandlerEntry.class));
	}
}