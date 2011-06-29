package ch.jester.ui.round.form;

public class ZestConnection {
	final String id; 
	final String label; 
	final ZestDataNode source;
	final ZestDataNode destination;
	
	public ZestConnection(String id, String label, ZestDataNode source, ZestDataNode destination) {
		this.id = id;
		this.label = label;
		this.source = source;
		this.destination = destination;
	}

	public String getLabel() {
		return label;
	}
	
	public ZestDataNode getSource() {
		return source;
	}
	public ZestDataNode getDestination() {
		return destination;
	}
	
}


