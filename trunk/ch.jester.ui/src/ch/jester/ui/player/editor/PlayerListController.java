package ch.jester.ui.player.editor;


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
import ch.jester.common.utility.ServiceUtility;
import ch.jester.dao.IPlayerPersister;
import ch.jester.model.Player;
import ch.jester.ui.Activator;


public class PlayerListController extends AbstractPropertyChangeModel{
	private List<Player> mPlayers = Collections.synchronizedList(new LinkedList<Player>());
	private TableViewer mViewer;
	private ServiceUtility mServices = Activator.getDefault().getActivationContext().getServiceUtil();
	IPlayerPersister persister = mServices.getExclusiveService(IPlayerPersister.class);
	ViewPart mPart;
	public PlayerListController(){

	}
	
	
	public List<Player> getPlayers() {
		return mPlayers;
	}
	
	public PlayerListController(ViewPart pPart, TableViewer pViewer){
		mViewer=pViewer;
		mPart=pPart;
		if (mPlayers != null) {
			initDataBindings();
		}
		Job job = new Job("Loading Players"){

			@Override
			protected IStatus run(IProgressMonitor monitor) {
				
				IPlayerPersister persister = Activator.getDefault().getActivationContext().getServiceUtil().getExclusiveService(IPlayerPersister.class);
				addPlayer(persister.getAll());
				persister.close();
				return Status.OK_STATUS;
			}
			
		};
		job.schedule();
	}
	
	public void addPlayer(Player pPlayer) {
		persister.save(pPlayer);
		mPlayers.add(pPlayer);
		firePropertyChange("players", null, mPlayers);
	}
	
	public void setPlayers(Collection<Player> pPlayerCollection){
		
		removeAll();
		addPlayer(pPlayerCollection);
		firePropertyChange("players", null, mPlayers);
		syncExe_refresh();
	}
	public void reloadPlayers(){
		
		removeAll();
		addPlayer(persister.getAll());
		firePropertyChange("players", null, mPlayers);
		syncExe_refresh();
	}
	
	
	public synchronized void addPlayer(Collection<Player> pPlayerCollection) {
		Object oldInput = mViewer.getInput();
		syncExe_setInput(null);		
		mPlayers.addAll(pPlayerCollection);	
		syncExe_setInput(oldInput);
		//syncExe_refresh();
	
	}
	public void removePlayer(Player pPlayer) {
		mPlayers.remove(pPlayer);
		firePropertyChange("players", null, mPlayers);
	}
	public void clear(){
		mPlayers.clear();
		firePropertyChange("players", null, mPlayers);
	}

	public void removePlayer(final List<Player> pPlayerList) {
		if(pPlayerList.size()>=0){
			removeManyPlayers(pPlayerList);
		}else{
			removeNotSoManyPlayers(pPlayerList);
		}
	}
	public void removeAll(){
		removeManyPlayers(mPlayers);
	}
	private void removeNotSoManyPlayers(List<Player> pPlayerList) {
		mPlayers.removeAll(pPlayerList);
		firePropertyChange("players", null, mPlayers);
	}

	private synchronized void removeManyPlayers(final List<Player> pPlayerList) {
				/*
				 * Workaround: Der Input wird direkt editiert.
				 * Bei einer Liste von > 10000 Einträgen werden entsprechend
				 * viele Events gefeuert, wenn alle Einträge auf 1 mal entfernt werden.
				 * (App-Freeze)
				 */
			
					final Object oldInput = mViewer.getInput();
					
					syncExe_setInput(null);
					
					mPlayers.removeAll(pPlayerList);
	
					syncExe_setInput(oldInput);

	}
	
	private void syncExe_setInput(final Object pInput){
		UIUtility.syncExecInUIThread(new Runnable(){

			@Override
			public void run() {
				mViewer.setInput(pInput);
				
			}
			
		});

	}
	
	private void syncExe_refresh(){
		UIUtility.syncExecInUIThread(new Runnable(){

			@Override
			public void run() {
				mViewer.refresh();
				
			}
			
		});

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
