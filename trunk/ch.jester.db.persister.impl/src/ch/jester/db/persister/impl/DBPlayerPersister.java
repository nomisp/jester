package ch.jester.db.persister.impl;

import java.util.List;

import ch.jester.dao.IPlayerPersister;
import ch.jester.model.Player;

public class DBPlayerPersister extends GenericPersister<Player> implements IPlayerPersister{

	@Override
	public List<Player> findByName(String pName) {
		return super.findByParameter("findByName", "lastName", "%"+pName.toUpperCase()+"%");
	}

}
