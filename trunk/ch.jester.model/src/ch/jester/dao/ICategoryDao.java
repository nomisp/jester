package ch.jester.dao;

import java.util.List;

import ch.jester.commonservices.api.persistency.IDaoService;
import ch.jester.model.Category;

public interface ICategoryDao extends IDaoService<Category> {

	public List<Category> findByName(String name);
}
