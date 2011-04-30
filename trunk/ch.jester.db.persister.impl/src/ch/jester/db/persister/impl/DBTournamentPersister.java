package ch.jester.db.persister.impl;

import java.util.List;

import ch.jester.dao.ITournamentDao;
import ch.jester.model.Tournament;

public class DBTournamentPersister extends GenericPersister<Tournament> implements ITournamentDao {

	@Override
	public List<Tournament> findByName(String name) {
		return super.findByParameter("TournamentByName", "name", prepareLikeSearch(name, MatchMode.ANYWHERE));
	}

}
