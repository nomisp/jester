package ch.jester.common.ui.databinding;

import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;

import ch.jester.common.ui.databinding.PageController.IPageControllerUIAccess;


/**
 * Defaultimplementierung von IPageControllerUIAccess Adpater für einen TableViewer 
 *
 */
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