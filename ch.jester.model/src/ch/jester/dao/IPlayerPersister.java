package ch.jester.dao;
import java.util.List;

import ch.jester.model.Player;

public interface IPlayerPersister extends IPersister<Player>{
	public List<Player> findByName(String pName);
}
