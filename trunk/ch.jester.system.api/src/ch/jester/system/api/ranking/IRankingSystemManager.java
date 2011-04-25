package ch.jester.system.api.ranking;

import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;

import ch.jester.commonservices.api.components.IEPEntryComponentService;
import ch.jester.model.Category;
import ch.jester.model.Player;
import ch.jester.system.exceptions.NotAllResultsException;

public interface IRankingSystemManager extends IEPEntryComponentService<IRankingSystemEntry, IRankingSystem> {

	/**
	 * Erstellt die Rangliste
	 * @param rankingSystem
	 * @param progressMonitor
	 * @return Liste mit den Spielern Index 0 = Rang 1
	 * @throws NotAllResultsException Es fehlen noch Resultate um die Rangliste zu erstellen
	 */
	public List<Player> doCalculateRanking(IRankingSystemEntry rankingSystem, IProgressMonitor progressMonitor) throws NotAllResultsException;
	
	/**
	 * Erstellt die Rangliste in einer Kategorie
	 * @param category
	 * @param rankingSystem
	 * @param progressMonitor
	 * @return Liste mit den Spielern Index 0 = Rang 1
	 * @throws NotAllResultsException Es fehlen noch Resultate um die Rangliste zu erstellen
	 */
	public List<Player> doCalculateRanking(Category category, IRankingSystemEntry rankingSystem, IProgressMonitor progressMonitor) throws NotAllResultsException;
	
}
