package ch.jester.ui.round.form;

import ch.jester.model.Pairing;
import ch.jester.model.Player;
import ch.jester.model.util.PlayerColor;
import ch.jester.model.util.Result;
import ch.jester.ui.round.editors.ResultController;

public class PlayerDataNode extends ZestDataNode {
	private static String NULL_ID = "playernode_null";
	private Pairing pairing;
	private PlayerColor color;
	private ResultController mController;
	public PlayerDataNode(String id, String name, Pairing pairing, Player data, PlayerColor c, ResultController pController) {
		super(id, name, data);
		color = c;
		this.pairing = pairing;
		mController = pController;
	}
	public static PlayerDataNode createNULLPlayerDataNode(Pairing p, PlayerColor c, ResultController ctrl){
		return new PlayerDataNode("playernode_null","",p, null, c, ctrl);
	}
	
	public boolean isNullPlayer(){
		return this.getId().equals(getNULLDataNodeID());
	}
	
	public static String getNULLDataNodeID(){
		return NULL_ID;
	}
	public boolean isBlack(){
		return color ==PlayerColor.BLACK;
	}
	
	public Player getPlayer(){
		return (Player) getData();
	}
	
	public Pairing getPairing(){
		return pairing;
	}

	public boolean isLoser(){
		return !isWinner();
	}
	
	public boolean isResultSet(){
		if(pairing==null){return false;}
		String pairingResult = mController.getLastPairingResultAsString(pairing);
		return pairingResult==null||pairingResult.isEmpty()?false:true;
	}
	
	
	public boolean isRemis(){
		if(pairing==null){return false;};
		String pairingResult = mController.getLastPairingResultAsString(pairing);
		if(pairingResult==null){return false;}
		return pairingResult.equals(Result.REMIS.getShortResult());
		
	}
	
	public boolean isWinner(){
		if(pairing==null){return false;};
		String pairingResult = mController.getLastPairingResultAsString(pairing);
		if(pairingResult==null){
			return false;
		}
		if(color == PlayerColor.BLACK){
			if(pairingResult.equals(Result.WHITE_ZERO.getShortResult())){
				return true;
			}
			if(pairingResult.equals(Result.WHITE_ZERO_F.getShortResult())){
				return true;
			}
			return false;
		}else{
			if(pairingResult.equals(Result.WHITE_ONE.getShortResult())){
				return true;
			}
			if(pairingResult.equals(Result.WHITE_ONE_F.getShortResult())){
				return true;
			}
			return false;
		}
	}
}
