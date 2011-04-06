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
import org.eclipse.jface.databinding.viewers.ObservableListContentProvider;
import org.eclipse.jface.databinding.viewers.ObservableMapLabelProvider;
import org.eclipse.jface.viewers.TableViewer;

import ch.jester.common.model.AbstractPropertyChangeModel;
import ch.jester.common.ui.utility.UIUtility;
import ch.jester.model.Player;


public class PlayerListController extends AbstractPropertyChangeModel{
	private List<Player> mPlayers = Collections.synchronizedList(new LinkedList<Player>());
	private TableViewer mViewer;
	private DataBindingContext mBindingContext;
	public List<Player> getPlayers() {
		return mPlayers;
	}
	
	public PlayerListController(TableViewer pViewer){
		mViewer=pViewer;
		if (mPlayers != null) {
			mBindingContext = initDataBindings();
		}
	}
	
	public void addPlayer(Player pPlayer) {

		mPlayers.add(pPlayer);
		firePropertyChange("players", null, mPlayers);
	}
	
	public void addPlayer(Collection<Player> pPlayerCollection) {
		mPlayers.addAll(pPlayerCollection);
		firePropertyChange("players", null, mPlayers);
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
		if(pPlayerList.size()>=500){
			removeManyPlayers(pPlayerList);
		}else{
			removeNotSoManyPlayers(pPlayerList);
		}
	}
	private void removeNotSoManyPlayers(List<Player> pPlayerList) {
		mPlayers.removeAll(pPlayerList);
		firePropertyChange("players", null, mPlayers);
	}

	private void removeManyPlayers(final List<Player> pPlayerList) {
				/*
				 * Workaround: Der Input wird direkt editiert.
				 * Bei einer Liste von > 10000 Einträgen werden entsprechend
				 * viele Events gefeuert, wenn alle Einträge auf 1 mal entfernt werden.
				 * (App-Freeze)
				 */
			
				final Object oldInput = mViewer.getInput();
						UIUtility.executeInUIThread(new Runnable(){

							@Override
							public void run() {
								mViewer.setInput(null);
								
							}
							
						});

						mPlayers.removeAll(pPlayerList);
	
						UIUtility.executeInUIThread(new Runnable(){

							@Override
							public void run() {
								mViewer.setInput(oldInput);
								
							}
							
						});
						
						//firePropertyChange("players", null, mPlayers);
				
	


	}
	protected DataBindingContext initDataBindings() {
		DataBindingContext bindingContext = new DataBindingContext();
		//
		ObservableListContentProvider listContentProvider = new ObservableListContentProvider();
		mViewer.setContentProvider(listContentProvider);
		//
		IObservableMap observeMap = BeansObservables.observeMap(listContentProvider.getKnownElements(), Player.class, "lastName");
		mViewer.setLabelProvider(new ObservableMapLabelProvider(observeMap));
		//
		IObservableList mPlayerListPlayersObserveList = BeansObservables.observeList(Realm.getDefault(), this, "players");
		mViewer.setInput(mPlayerListPlayersObserveList);
		//
		return bindingContext;
	}

}
