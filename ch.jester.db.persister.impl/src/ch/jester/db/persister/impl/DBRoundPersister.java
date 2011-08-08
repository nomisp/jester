package ch.jester.db.persister.impl;

import java.util.List;

import ch.jester.dao.IRoundDao;
import ch.jester.model.Round;

/**
 * Impl eines {@link IRoundDao} für die Persistierung von Runden
 *
 */
public class DBRoundPersister extends GenericPersister<Round> implements IRoundDao {

	
	
	@Override
	public List<Round> findByNumber(Integer number) {
		return super.executeNamedQuery("RoundByNumber", "number", number);
	}

}
