package ch.jester.system.api.ranking;

import java.util.Map;

import org.eclipse.core.runtime.IProgressMonitor;

import ch.jester.commonservices.api.components.IEPEntryComponentService;
import ch.jester.model.Category;
import ch.jester.model.Ranking;
import ch.jester.model.Tournament;
import ch.jester.system.exceptions.NotAllResultsException;

public interface IRankingSystemManager extends IEPEntryComponentService<IRankingSystemEntry, IRankingSystem> {

	/**
	 * Erstellt die Rangliste eines Turniers
	 * @param tournament
	 * @param rankingSystem
	 * @param progressMonitor
	 * @return Map mit den Rankings je Kategorie
	 * @throws NotAllResultsException Es fehlen noch Resultate um die Rangliste zu erstellen
	 */
	public Map<Category, Ranking> doCalculateRanking(Tournament tournament, IRankingSystemEntry rankingSystem, IProgressMonitor progressMonitor) throws NotAllResultsException;
	
	/**
	 * Erstellt die Rangliste in einer Kategorie
	 * @param category
	 * @param rankingSystem
	 * @param progressMonitor
	 * @return Ranking der Kategorie
	 * @throws NotAllResultsException Es fehlen noch Resultate um die Rangliste zu erstellen
	 */
	public Ranking doCalculateRanking(Category category, IRankingSystemEntry rankingSystem, IProgressMonitor progressMonitor) throws NotAllResultsException;
	
}
