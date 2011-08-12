package ch.jester.ui.round.form.contentprovider;

import java.util.ArrayList;
import java.util.List;

import messages.Messages;

import ch.jester.model.Pairing;
import ch.jester.model.Player;
import ch.jester.model.Round;
import ch.jester.model.util.PlayerColor;
import ch.jester.ui.round.editors.ResultController;
import ch.jester.ui.round.form.PlayerDataNode;
import ch.jester.ui.round.form.ZestDataNode;

/**
 * Hilfklasse um die ZestNodes zu verbinden.
 *
 */
public class RoundNodeModelContentProvider {
	//private List<ZestConnection> connections;
	private List<ZestDataNode> allnodes;
	private List<ZestDataNode> parentNodes;
	protected Object mInput;
	private ZestUtil util = new ZestUtil();
	private ResultController mController;
	public RoundNodeModelContentProvider(ResultController controller) {
		mController = controller;
	}

	public List<ZestDataNode> getAllNodes() {
		return allnodes;
	}
	public List<ZestDataNode> getParentNodes() {
		return parentNodes;
	}

	public void setNode(List<ZestDataNode> pNodes){
		allnodes = pNodes;
	}
	protected void buildRound(Round pRound){
		allnodes = new ArrayList<ZestDataNode>();
		parentNodes = new ArrayList<ZestDataNode>();
		//connections = new ArrayList<ZestConnection>();
		List<Pairing> pairings = pRound.getPairings();
		for(Pairing p:pairings){
			ZestDataNode pairingNode = new ZestDataNode(p.getId()+"", Messages.TournamentLabelProvider_lbl_pairing, p);
			parentNodes.add(pairingNode);
			Player p1 = p.getBlack().getPlayer();
			Player p2 = p.getWhite().getPlayer();
			
			PlayerDataNode blackNode = createPlayerNode(p, p1, PlayerColor.BLACK);
			PlayerDataNode whiteNode = createPlayerNode(p, p2, PlayerColor.WHITE);
			allnodes.add(pairingNode);
			if(!whiteNode.isNullPlayer()){
				util.connect(pairingNode, whiteNode);
				allnodes.add(whiteNode);
			}
			if(!blackNode.isNullPlayer()){
				util.connect(pairingNode, blackNode);
				allnodes.add(blackNode);
			}
			//allnodes.add(blackNode);
			//allnodes.add(whiteNode);
			//util.connect(pnode1, pnode2);
		}
		util.establishConnections();
	}
	private PlayerDataNode createPlayerNode(Pairing p, Player p1, PlayerColor c){
		if(p1!=null){
			return new PlayerDataNode(p1.getId()+"", p1.getLastName()+", "+p1.getFirstName(),p, p1, c, mController);
		}else{
			return PlayerDataNode.createNULLPlayerDataNode(p, c, mController);
		}
	}

	public Object getInput() {
		return mInput;
	}

	public void setInput(Object input) {
		mInput=input;
		buildRound((Round) mInput);
	}
}



