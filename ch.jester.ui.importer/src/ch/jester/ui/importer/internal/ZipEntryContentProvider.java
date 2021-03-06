package ch.jester.ui.importer.internal;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

import ch.jester.common.utility.ZipUtility;

public class ZipEntryContentProvider implements IStructuredContentProvider{
	String pFileInput;
	private Controller mController;
	public ZipEntryContentProvider(Controller pController){
		mController = pController;
	}
	
	@Override
	public void dispose() {}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput,
			Object newInput) {
		if(newInput!=null){
			pFileInput=newInput.toString();
		}
	}

	@Override
	public Object[] getElements(Object inputElement) {
		return mController.filterZipEntries(ZipUtility.getZipEntries(pFileInput, false).toArray());
	}
}
