package ch.jester.ui.contentprovider;

import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;

import ch.jester.ui.contentprovider.PageController.IPageControllerUIAccess;

public class TableViewerAdapter implements IPageControllerUIAccess{
	private TableViewer mViewer;
	public TableViewerAdapter(TableViewer pViewer){
		mViewer = pViewer;
	}

	@Override
	public Object getFirstElement() {
		return mViewer.getElementAt(0);
	}

	@Override
	public void setSelection(Object pSObject, boolean reveal) {
		 mViewer.setSelection(new StructuredSelection(pSObject), reveal);
	}

	@Override
	public void setInput(Object pInput) {
		mViewer.setInput(pInput);
	}
}