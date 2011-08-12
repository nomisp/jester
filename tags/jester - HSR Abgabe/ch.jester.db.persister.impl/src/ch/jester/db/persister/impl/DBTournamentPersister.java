package ch.jester.db.persister.impl;

import java.util.List;

import javax.persistence.Query;

import ch.jester.dao.ITournamentDao;
import ch.jester.model.Tournament;

/**
 * Impl von {@link ITournamentDao} für die Persistierung von Turnieren
 *
 */
public class DBTournamentPersister extends GenericPersister<Tournament> implements ITournamentDao {

	@Override
	public List<Tournament> findByName(String name) {
		return super.executeNamedQuery("TournamentByName", "name",
				prepareLikeSearch(name, MatchMode.ANYWHERE));
	}

	@Override
	protected Query getCountQuery() {
		return super.getManager().createNamedQuery("countTournaments");
	}

	@Override
	protected Query getPagingQuery() {
		return super.getManager().createNamedQuery("AllTournaments");
	}
}
