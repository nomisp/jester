package ch.jester.ui.player.editor;

import org.eclipse.jface.viewers.ILazyContentProvider;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;

import ch.jester.commonservices.util.ServiceUtility;
import ch.jester.dao.IPlayerPersister;
import ch.jester.dao.ScrollableResultListJPA;
import ch.jester.model.Player;

class QueryContentProvider implements ILazyContentProvider {
	ServiceUtility su = new ServiceUtility();
	IPlayerPersister persister = su.getExclusiveService(IPlayerPersister.class);
	ScrollableResultListJPA<Player> list = new ScrollableResultListJPA<Player>(persister, 50);
	TableViewer mViewer;
	public QueryContentProvider(TableViewer pViewer){
		mViewer = pViewer;
	}
	
	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		mViewer.setItemCount(list.size());
		
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