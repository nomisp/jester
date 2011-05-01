package ch.jester.dao;

import java.util.List;

import ch.jester.model.Tournament;

public interface ITournamentDao extends IDaoService<Tournament> {

	public List<Tournament> findByName(String name);
}
