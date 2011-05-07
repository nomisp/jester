package ch.jester.common.ui.databinding;

import org.eclipse.jface.viewers.ILazyContentProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;

import ch.jester.common.persistency.util.ScrollableResultListJPA;
import ch.jester.commonservices.api.persistency.IDaoObject;
import ch.jester.commonservices.api.persistency.IDaoService;
import ch.jester.commonservices.util.ServiceUtility;

/**
 * Ein ContentProvider der Daten beim Scrollen von der DB nachlädt
 *
 */
public class ScrollableContentProvider<T extends IDaoObject> implements ILazyContentProvider {
	ServiceUtility su = new ServiceUtility();
	IDaoService<T> persister;
	ScrollableResultListJPA<T> list;
	TableViewer mViewer;
	public ScrollableContentProvider(TableViewer pViewer, IDaoService<T> pPersister, int pPageSize){
		mViewer = pViewer;
		persister = pPersister;
		list = new ScrollableResultListJPA<T>(persister, pPageSize);
		
	}
	
	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		mViewer.setItemCount(list.size());
		
	}
	
	public int getTotalEntries(){
		return list.size();
	}
	
	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void updateElement(int index) {
		mViewer.replace(list.get(index), index);
		
	}
};