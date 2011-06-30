package ch.jester.ui.tournament.cnf;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

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
		players = checkNullPlayers(players);
		if (players != null) return players;
		return EMPTY_ARRAY;
	}

	private Object[] checkNullPlayers(Object[] players) {
		List<Object> list = new ArrayList<Object>(Arrays.asList(players));
		list.removeAll(Collections.singleton(null));
		return list.toArray();
	}
}