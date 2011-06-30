package ch.jester.ui.round.form;

import ch.jester.model.Pairing;
import ch.jester.model.Player;
import ch.jester.model.Result;
import ch.jester.ui.round.editors.ResultController;

public class PlayerDataNode extends ZestDataNode {
	public enum PlayerColor{
		B, W;
	}
	private Pairing pairing;
	private PlayerColor color;
	private ResultController mController;
	public PlayerDataNode(String id, String name, Pairing pairing, Player data, PlayerColor c, ResultController pController) {
		super(id, name, data);
		color = c;
		this.pairing = pairing;
		mController = pController;
	}
	
	public boolean isBlack(){
		return color ==PlayerColor.B;
	}
	
	public Player getPlayer(){
		return (Player) getData();
	}
	
	public Pairing getPairing(){
		return pairing;
	}

	public boolean hasWon(){
		if(pairing==null){return false;};
		String pairingResult = null;
		Result changedResult =  mController.getChangedResults().get(pairing);
		if(changedResult == null){
			pairingResult = pairing.getResult();
		}else{
			pairingResult = changedResult.getShortResult();
		}
		if(pairingResult==null){
			return false;
		}
		if(color == PlayerColor.B){
			if(pairingResult.equals(Result.BLACK_WINS.getShortResult())){
				return true;
			}
			if(pairingResult.equals(Result.BLACK_WINS_F.getShortResult())){
				return true;
			}
			return false;
		}else{
			if(pairingResult.equals(Result.WHITE_WINS.getShortResult())){
				return true;
			}
			if(pairingResult.equals(Result.WHITE_WINS_F.getShortResult())){
				return true;
			}
			return false;
		}
	}
}
