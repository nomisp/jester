package ch.jester.dao;
import java.util.List;

import ch.jester.commonservices.api.persistency.IDaoService;
import ch.jester.model.Player;

/**
 * DAO Interface für Player
 *
 */
public interface IPlayerDao extends IDaoService<Player>{
	public List<Player> findByName(String pName);
	public List<Player> findByNamesOrCity(String pName);
}
