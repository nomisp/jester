package ch.jester.db.persister.impl;

import java.util.List;

import javax.persistence.Query;

import ch.jester.dao.IPlayerDao;
import ch.jester.model.Player;

/**
 * Implementierung für die Persistenz von Playern
 *
 */
public class DBPlayerPersister extends GenericPersister<Player> implements IPlayerDao{

	
	@Override
	public List<Player> findByName(String pName) {
		return super.executeNamedQuery("findByName", "lastName", prepareLikeSearch(pName.toUpperCase(), MatchMode.ANYWHERE));
	}
	 @Override
	protected Query getCountQuery() {
		return super.mManager.createNamedQuery("count");
	}
	 @Override
	protected Query getPagingQuery() {
		return super.mManager.createNamedQuery("getAll");
	}
}
