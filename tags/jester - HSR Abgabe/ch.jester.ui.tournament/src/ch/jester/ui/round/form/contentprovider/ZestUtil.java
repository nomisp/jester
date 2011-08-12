package ch.jester.ui.round.form.contentprovider;

import java.util.ArrayList;
import java.util.List;

import ch.jester.ui.round.form.ZestConnection;
import ch.jester.ui.round.form.ZestDataNode;

/**
 * Hilfklasse um die ZestNodes zu verbinden.
 *
 */
public class ZestUtil {
	private List<ZestConnection> connections = new ArrayList<ZestConnection>();
	public ZestConnection connect(String pId, String pLabel, ZestDataNode parent, ZestDataNode child){
		ZestConnection c =  new ZestConnection(pId, pLabel, parent, child);
		connections.add(c);
		return c;
	}
	public List<ZestConnection> connect(String pId, String pLabel, ZestDataNode parent, List<ZestDataNode> children){
		List<ZestConnection> subc = new ArrayList<ZestConnection>();
		for(ZestDataNode child:children){
			subc.add(connect(pId, pLabel, parent, child));
		}
		connections.addAll(subc);
		return subc;
	}
	public ZestConnection connect(ZestDataNode parent, ZestDataNode child){
		return connect(null, null, parent, child);
	}
	public List<ZestConnection> connect(ZestDataNode parent, List<ZestDataNode> children){
		return connect(null, null, parent, children);
	}
	
	public void establishConnections(){
		for (ZestConnection connection : connections) {
			connection.getSource().getConnectedTo().add(connection.getDestination());
		}
	}

	
	public List<ZestConnection> getConnections(){
		return connections;
	}
}
