package ch.jester.ui.tournament.cnf;

import ch.jester.model.Category;

public class PlayerFolder {
	private Object[] players;
	private Object parent;
	private static final Object[] EMPTY_ARRAY = new Object[0];
	public PlayerFolder(Category cat) {
		parent = cat;
		players = getPlayers(cat);
	}
	
	public Object[] getElements() {
		return players;
	}
	
	public Object getParent() {
		return parent;
	}
	private Object[] getPlayers(Category category) {
		Object[] players = category.getPlayers().toArray();
		if (players != null) return players;
		return EMPTY_ARRAY;
	}
}