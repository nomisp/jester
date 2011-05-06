package ch.jester.ui.contentprovider;

import org.eclipse.jface.viewers.ILazyContentProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;

import ch.jester.commonservices.util.ServiceUtility;
import ch.jester.dao.IPlayerDao;
import ch.jester.dao.ScrollableResultListJPA;
import ch.jester.model.Player;

/**
 * Ein ContentProvider der Daten beim Scrollen von der DB nachlädt
 *
 */
public class ScrollableContentProvider implements ILazyContentProvider {
	ServiceUtility su = new ServiceUtility();
	IPlayerDao persister = su.getExclusiveService(IPlayerDao.class);
	ScrollableResultListJPA<Player> list;
	TableViewer mViewer;
	public ScrollableContentProvider(TableViewer pViewer, int pPageSize){
		mViewer = pViewer;
		
		list = new ScrollableResultListJPA<Player>(persister, pPageSize);
		
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