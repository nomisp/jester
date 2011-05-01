package ch.jester.dao;
import java.util.List;

import ch.jester.model.Player;

public interface IPlayerDao extends IDaoService<Player>{
	public List<Player> findByName(String pName);
}
