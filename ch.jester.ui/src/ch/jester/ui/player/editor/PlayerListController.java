package ch.jester.ui.player.editor;


import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.observable.map.IObservableMap;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.databinding.viewers.ObservableMapLabelProvider;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.ui.part.ViewPart;

import ch.jester.common.model.AbstractPropertyChangeModel;
import ch.jester.common.ui.utility.UIUtility;
import ch.jester.commonservices.api.persistencyevent.IPersistencyEventQueue;
import ch.jester.commonservices.api.persistencyevent.IPersistencyListener;
import ch.jester.commonservices.api.persistencyevent.PersistencyEvent;
import ch.jester.commonservices.util.ServiceUtility;
import ch.jester.dao.IPlayerDao;
import ch.jester.model.Player;
import ch.jester.ui.Activator;
import ch.jester.ui.contentprovider.PagingContentProvider;


public class PlayerListController extends AbstractPropertyChangeModel{
	//private List<Player> mPlayers = new ArrayList<Player>();
	private TableViewer mViewer;
	private ServiceUtility mServices = Activator.getDefault().getActivationContext().getServiceUtil();
	IPlayerDao persister = mServices.getExclusiveService(IPlayerDao.class);
	ViewPart mPart;
	PagingContentProvider contentProvider;
	public PlayerListController(){

	}
	
	public PlayerListController(ViewPart pPart, TableViewer pViewer){
		mServices.getService(IPersistencyEventQueue.class).addListener(new IPersistencyListener() {
			@Override
			public void persistencyEvent(PersistencyEvent event) {
				if(event.getSource()!=persister){
					System.out.println("refresh");
					reloadPlayers();
				}
				
			}
		});

		mViewer=pViewer;
		mPart=pPart;
		initDataBindings();
		Job job = new Job("Loading Players"){

			@Override
			protected IStatus run(IProgressMonitor monitor) {
				reloadPlayers();
				return Status.OK_STATUS;
			}
			
		};
		job.schedule();
	}
	
	private void reloadPlayers(){
		contentProvider.refresh();
	}
	
	
	public void addPlayer(Player pPlayer) {
		addPlayer(createList(pPlayer));
	}
	public void addPlayer(Collection<Player> pPlayerCollection) {
		persister.save(pPlayerCollection);
		contentProvider.refresh();
	}
	
	
	public void removePlayer(Player pPlayer) {
		removePlayer(createList(pPlayer));
	}
	public void removePlayer(List<Player> pPlayerList) {
		persister.delete(pPlayerList);
		contentProvider.refresh();
	}
	
	/**
	 * Für die Search
	 * @param pPlayerCollection
	 */
	public void setSearched(final Collection<Player> pPlayerCollection){
		UIUtility.syncExecInUIThread(new Runnable() {
			
			@Override
			public void run() {
				mViewer.setContentProvider(ArrayContentProvider.getInstance());
				mViewer.setInput(pPlayerCollection);
				
			}
		});

		
	}
	public void clearSearched(){
		UIUtility.syncExecInUIThread(new Runnable() {
			
			@Override
			public void run() {
				mViewer.setContentProvider(contentProvider);
				contentProvider.refresh();
				
			}
		});
	
	}


	protected DataBindingContext initDataBindings() {
		
		contentProvider = new PagingContentProvider(mViewer, 1000);
		mViewer.setContentProvider(contentProvider);
		return null;
	}
	
	private List<Player> createList(Player o){
		ArrayList<Player> list = new ArrayList<Player>();
		list.add(o);
		return list;
	}
	class PlayerMapLabelProvider extends ObservableMapLabelProvider{

		public PlayerMapLabelProvider(IObservableMap[] attributeMaps) {
			super(attributeMaps);
			
		}
		@Override
		public String getColumnText(Object element, int columnIndex) {
			// TODO Auto-generated method stub
			return getText(element);
		}
		@Override
		public String getText(Object element) {
			if(element instanceof Player){
				Player p = (Player) element;
				return p.getLastName()+", "+p.getFirstName();
			}
			return super.getText(element);
		}


		
	}

	public PagingContentProvider getPageController() {
	
		return this.contentProvider;
	}
}
