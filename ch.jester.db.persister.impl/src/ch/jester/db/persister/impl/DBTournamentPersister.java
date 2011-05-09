package ch.jester.db.persister.impl;

import java.util.List;

import javax.persistence.Query;

import ch.jester.dao.ITournamentDao;
import ch.jester.model.Tournament;

public class DBTournamentPersister extends GenericPersister<Tournament> implements ITournamentDao {

	public DBTournamentPersister() {
		super(Tournament.class);
	}

	@Override
	public List<Tournament> findByName(String name) {
		return super.findByParameter("TournamentByName", "name",
				prepareLikeSearch(name, MatchMode.ANYWHERE));
	}

	@Override
	protected Query getCountQuery() {
		return super.mManager.createNamedQuery("countTournaments");
	}

	@Override
	protected Query getPagingQuery() {
		return super.mManager.createNamedQuery("AllTournaments");
	}
}
