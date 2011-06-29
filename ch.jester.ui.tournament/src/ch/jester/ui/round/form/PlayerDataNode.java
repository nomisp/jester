package ch.jester.ui.round.form;

import ch.jester.model.Player;

public class PlayerDataNode extends ZestDataNode {
	public enum PlayerColor{
		B, W;
	}
	private PlayerColor color;
	public PlayerDataNode(String id, String name, Player data, PlayerColor c) {
		super(id, name, data);
		color = c;
	}
	
	public boolean isBlack(){
		return color ==PlayerColor.B;
	}
	
	public Player getPlayer(){
		return (Player) getData();
	}

}
