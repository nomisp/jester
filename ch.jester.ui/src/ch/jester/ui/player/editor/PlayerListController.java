package ch.jester.ui.player.editor;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.BeansObservables;
import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.core.databinding.observable.list.IObservableList;
import org.eclipse.core.databinding.observable.map.IObservableMap;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.databinding.viewers.ObservableListContentProvider;
import org.eclipse.jface.databinding.viewers.ObservableMapLabelProvider;
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


public class PlayerListController extends AbstractPropertyChangeModel{
	private List<Player> mPlayers = Collections.synchronizedList(new LinkedList<Player>());
	private TableViewer mViewer;
	private ServiceUtility mServices = Activator.getDefault().getActivationContext().getServiceUtil();
	IPlayerDao persister = mServices.getExclusiveService(IPlayerDao.class);
	ViewPart mPart;
	public PlayerListController(){

	}
	
	
	public List<Player> getPlayers() {
		return mPlayers;
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
		if (mPlayers != null) {
			initDataBindings();
		}
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
		Object oldInput = mViewer.getInput();
		syncedUI_setInput(null);
		mPlayers.clear();
		mPlayers.addAll(persister.getAll());
		syncedUI_setInput(oldInput);
		
		/*mPlayers.clear();
		mPlayers.addAll(persister.getAll());
		firePropertyChange("players", null, mPlayers);*/
	}
	
	
	public void addPlayer(Player pPlayer) {
		addPlayer(createList(pPlayer));
	}
	public void addPlayer(Collection<Player> pPlayerCollection) {
		persister.save(pPlayerCollection);
		mPlayers.addAll(pPlayerCollection);
		firePropertyChange("players", null, mPlayers);
	}
	
	
	public void removePlayer(Player pPlayer) {
		removePlayer(createList(pPlayer));
	}
	public void removePlayer(List<Player> pPlayerList) {
		Object oldInput = mViewer.getInput();
		syncedUI_setInput(null);
		persister.delete(pPlayerList);
		mPlayers.removeAll(pPlayerList);
		syncedUI_setInput(oldInput);
		//firePropertyChange("players", null, mPlayers);
	}
	
	/**
	 * Für die Search
	 * @param pPlayerCollection
	 */
	public void setPlayers(Collection<Player> pPlayerCollection){
		final Object oldInput = mViewer.getInput();
		syncedUI_setInput(null);
		mPlayers.clear();
		mPlayers.addAll(pPlayerCollection);
		syncedUI_setInput(oldInput);
	}

	private void syncedUI_setInput(final Object pInput){
		UIUtility.syncExecInUIThread(new Runnable(){
			@Override
			public void run() {
				if(pInput==null){
					mViewer.getControl().setRedraw(false);
				}
				mViewer.setInput(pInput);
				if(pInput!=null){
					mViewer.getControl().setRedraw(true);
				}
			}

		});
	}


	

	public void clear(){
		mPlayers.clear();
		firePropertyChange("players", null, mPlayers);
	}



	protected DataBindingContext initDataBindings() {
		DataBindingContext bindingContext = new DataBindingContext();
		//
		ObservableListContentProvider listContentProvider = new ObservableListContentProvider();
		mViewer.setContentProvider(listContentProvider);
		//
		IObservableMap[] observeMap = BeansObservables.observeMaps(listContentProvider.getKnownElements(), Player.class, new String[]{"lastName","firstName"});
		mViewer.setLabelProvider(new PlayerMapLabelProvider(observeMap));
		//
		IObservableList mPlayerListPlayersObserveList = BeansObservables.observeList(Realm.getDefault(), this, "players");
		mViewer.setInput(mPlayerListPlayersObserveList);
		//
		return bindingContext;
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
}
