package ch.jester.db.persister.impl;

import java.util.List;

import ch.jester.dao.ICategoryDao;
import ch.jester.model.Category;
import ch.jester.model.Player;

public class DBCategoryPersister extends GenericPersister<Category> implements ICategoryDao {


	@Override
	public List<Category> findByName(String name) {
		return super.findByParameter("CategoryByName", "description",
				prepareLikeSearch(name, MatchMode.ANYWHERE));
	}

	@Override
	public Category findByPlayer(Player player) {
		return super.findByParameter("CategoryByPlayer", "player", player).get(0);
	}

}
