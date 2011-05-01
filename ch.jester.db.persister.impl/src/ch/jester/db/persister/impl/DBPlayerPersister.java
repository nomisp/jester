package ch.jester.db.persister.impl;

import java.util.List;

import ch.jester.dao.IPlayerDao;
import ch.jester.model.Player;

public class DBPlayerPersister extends GenericPersister<Player> implements IPlayerDao{

	@Override
	public List<Player> findByName(String pName) {
		return super.findByParameter("findByName", "lastName", prepareLikeSearch(pName.toUpperCase(), MatchMode.ANYWHERE));
	}

}
