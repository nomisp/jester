package ch.jester.ui.importer.internal;

import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;

import ch.jester.common.ui.utility.SelectionUtility;
import ch.jester.commonservices.api.importer.IImportHandlerEntry;

public class HandlerSelectionListener implements ISelectionChangedListener {
	SelectionUtility su = new SelectionUtility(null);
	public HandlerSelectionListener() {

	}
	@Override
	public void selectionChanged(SelectionChangedEvent event) {
		su.setSelection(event.getSelection());
		Controller.getController().setSelectedHandlerEntry(su.getFirstSelectedAs(IImportHandlerEntry.class));
	}
}