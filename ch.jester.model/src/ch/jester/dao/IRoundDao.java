package ch.jester.dao;

import java.util.List;

import ch.jester.commonservices.api.persistency.IDaoService;
import ch.jester.model.Round;

/**
 * DAO Interface für Runden
 *
 */
public interface IRoundDao extends IDaoService<Round> {

	public List<Round> findByNumber(Integer number);
}
