package ch.jester.ui.player;


import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import ch.jester.common.model.AbstractPropertyChangeModel;
import ch.jester.model.Player;


public class PlayerListModel extends AbstractPropertyChangeModel{
	List<Player> mPlayers = new LinkedList<Player>();
	
	public List<Player> getPlayers() {
		return mPlayers;
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


	public void removePlayer(List<Player> pPlayerList) {
		
		mPlayers.removeAll(pPlayerList);
		
		firePropertyChange("players", null, mPlayers);
	}
	

}
