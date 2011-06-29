package ch.jester.ui.round.form.contentprovider;

import java.util.ArrayList;
import java.util.List;

import ch.jester.ui.round.form.MyConnection;
import ch.jester.ui.round.form.MyNode;

public class ZestUtil {
	private List<MyConnection> connections = new ArrayList<MyConnection>();
	public MyConnection connect(String pId, String pLabel, MyNode parent, MyNode child){
		MyConnection c =  new MyConnection(pId, pLabel, parent, child);
		connections.add(c);
		return c;
	}
	public List<MyConnection> connect(String pId, String pLabel, MyNode parent, List<MyNode> children){
		List<MyConnection> subc = new ArrayList<MyConnection>();
		for(MyNode child:children){
			subc.add(connect(pId, pLabel, parent, child));
		}
		connections.addAll(subc);
		return subc;
	}
	public MyConnection connect(MyNode parent, MyNode child){
		return connect(null, null, parent, child);
	}
	public List<MyConnection> connect(MyNode parent, List<MyNode> children){
		return connect(null, null, parent, children);
	}
	
	public void establishConnections(){
		for (MyConnection connection : connections) {
			connection.getSource().getConnectedTo().add(connection.getDestination());
		}
	}
	
	public List<MyConnection> getConnections(){
		return connections;
	}
	
}
