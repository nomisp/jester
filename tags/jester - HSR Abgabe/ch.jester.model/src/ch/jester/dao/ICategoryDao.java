package ch.jester.dao;

import java.util.List;

import ch.jester.commonservices.api.persistency.IDaoService;
import ch.jester.model.Category;
import ch.jester.model.Player;

/**
 * DAO Interface für Kategorien
 *
 */
public interface ICategoryDao extends IDaoService<Category> {

	public List<Category> findByName(String name);
	
	public Category findByPlayer(Player player);
}
