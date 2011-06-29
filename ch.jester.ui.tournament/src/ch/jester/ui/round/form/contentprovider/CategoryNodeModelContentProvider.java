package ch.jester.ui.round.form.contentprovider;

import java.util.ArrayList;
import java.util.List;

import ch.jester.model.Category;
import ch.jester.model.Round;
import ch.jester.ui.round.form.MyConnection;
import ch.jester.ui.round.form.MyNode;

public class CategoryNodeModelContentProvider extends RoundNodeModelContentProvider{
	private ZestUtil util = new ZestUtil();
	private List<MyNode> nodes;

	public void setInput(Object input) {
		buildCategory((Category) input);

		
	}

	private void buildCategory(Category input) {
		nodes = new ArrayList<MyNode>();

		List<Round> rounds = input.getRounds();
		for(Round r:input.getRounds()){
			MyNode parentRound = new MyNode(r.getId()+"", "Round "+r.getNumber());
			nodes.add(parentRound);
			super.buildRound(r);
			List<MyNode> roundNodes = super.getNodes();
			util.connect(parentRound, roundNodes);
			util.establishConnections();
		}
		setNode(nodes);
	}
}
