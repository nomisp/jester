package ch.jester.ui.round.form.contentprovider;

import java.util.ArrayList;
import java.util.List;

import ch.jester.model.Pairing;
import ch.jester.model.Player;
import ch.jester.model.Round;
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
	public RoundNodeModelContentProvider() {
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
			Player p1 = p.getBlack();
			Player p2 = p.getWhite();
			
			ZestDataNode pnode1 = new PlayerDataNode(p1.getId()+"", p1.getLastName()+", "+p1.getFirstName(), p1, PlayerColor.B);
			ZestDataNode pnode2 = new PlayerDataNode(p2.getId()+"", p2.getLastName()+", "+p2.getFirstName(), p2, PlayerColor.W);
			allnodes.add(pairingNode);
			util.connect(pairingNode, pnode1);
			util.connect(pairingNode, pnode2);
			allnodes.add(pnode1);
			allnodes.add(pnode2);
			//util.connect(pnode1, pnode2);
		}
		util.establishConnections();
	}

	public Object getInput() {
		return mInput;
	}

	public void setInput(Object input) {
		mInput=input;
		buildRound((Round) mInput);
	}
}



