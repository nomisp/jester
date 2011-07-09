package ch.jester.ui.round.form.contentprovider;

import java.util.ArrayList;
import java.util.List;

import ch.jester.model.Pairing;
import ch.jester.model.Player;
import ch.jester.model.Round;
import ch.jester.ui.round.editors.ResultController;
import ch.jester.ui.round.form.PlayerDataNode;
import ch.jester.ui.round.form.ZestConnection;
import ch.jester.ui.round.form.ZestDataNode;
import ch.jester.ui.round.form.PlayerDataNode.PlayerColor;

public class RoundNodeModelContentProvider {
	private List<ZestConnection> connections;
	private List<ZestDataNode> allnodes;
	private List<ZestDataNode> parentNodes;
	protected Object mInput;
	private ZestUtil util = new ZestUtil();
	private ResultController mController;
	public RoundNodeModelContentProvider(ResultController controller) {
		mController = controller;
		// Image here a fancy DB access
		// Now create a few nodes
	/*	nodes = new ArrayList<MyNode>();
		MyNode node = new MyNode("1", "Hamburg");
		nodes.add(node);
		node = new MyNode("2", "Frankfurt");
		nodes.add(node);
		node = new MyNode("3", "Berlin");
		nodes.add(node);
		node = new MyNode("4", "Munich");
		nodes.add(node);
		node = new MyNode("5", "Eppelheim");
		nodes.add(node);
		node = new MyNode("6", "Ahrensboek");
		nodes.add(node);

		connections = new ArrayList<MyConnection>();
		MyConnection connect = new MyConnection("1", "1", nodes.get(0),
				nodes.get(1));
		connections.add(connect);
		connect = new MyConnection("2", "2", nodes.get(0), nodes.get(4));
		connections.add(connect);
		connect = new MyConnection("3", "3", nodes.get(2), nodes.get(1));
		connections.add(connect);
		connect = new MyConnection("4", "3", nodes.get(1), nodes.get(3));
		connections.add(connect);

		// Because we are lasy we save the info about the connections in the
		// nodes

		for (MyConnection connection : connections) {
			connection.getSource().getConnectedTo()
					.add(connection.getDestination());
		}*/
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
		connections = new ArrayList<ZestConnection>();
		List<Pairing> pairings = pRound.getPairings();
		for(Pairing p:pairings){
			ZestDataNode pairingNode = new ZestDataNode(p.getId()+"", "Pairing ", p);
			parentNodes.add(pairingNode);
			Player p1 = p.getBlack().getPlayer();
			Player p2 = p.getWhite().getPlayer();
			
			PlayerDataNode blackNode = createPlayerNode(p, p1, PlayerColor.B);
			PlayerDataNode whiteNode = createPlayerNode(p, p2, PlayerColor.W);
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


