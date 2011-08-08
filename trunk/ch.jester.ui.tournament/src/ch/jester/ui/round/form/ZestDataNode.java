package ch.jester.ui.round.form;

import java.util.ArrayList;
import java.util.List;

/**
 * Allgemeiner DataNode für ZEST
 *
 */
public class ZestDataNode {
	private final String id;
	private final String name;
	private List<ZestDataNode> connections;
	private Object mData;
	public ZestDataNode(String id, String name, Object data) {
		this.id = id;
		this.name = name;
		this.connections = new ArrayList<ZestDataNode>();
		mData = data;
	}

	public Object getData(){
		return  mData;
	}
	
	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public List<ZestDataNode> getConnectedTo() {
		return connections;
	}
	

}


