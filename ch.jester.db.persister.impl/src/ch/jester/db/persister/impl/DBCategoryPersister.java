package ch.jester.db.persister.impl;

import java.util.List;

import ch.jester.dao.ICategoryDao;
import ch.jester.model.Category;

public class DBCategoryPersister extends GenericPersister<Category> implements ICategoryDao {


	@Override
	public List<Category> findByName(String name) {
		return super.findByParameter("CategoryByName", "description",
				prepareLikeSearch(name, MatchMode.ANYWHERE));
	}

}
