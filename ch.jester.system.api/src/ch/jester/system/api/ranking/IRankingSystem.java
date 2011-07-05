package ch.jester.system.api.ranking;

import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.IProgressMonitor;

import ch.jester.model.Category;
import ch.jester.model.PlayerCard;
import ch.jester.model.Tournament;
import ch.jester.system.exceptions.NotAllResultsException;

/**
 * Interface für die Feinwertungen zur Berechnung
 * der Rangliste. Die verschiedenen Feinwertungen 
 * müssen dieses Interface implementieren.
 * @author Peter
 *
 */
public interface IRankingSystem {

	/**
	 * Berechnen der Rangliste
	 * @param tournament Turnier zu dem die Rangliste erstellt werden soll.
	 * @param pMonitor ProgrssMonitor
	 * @return Map mit den Ranglisten. Key ist die Category value die Liste mit den Spielern Index 0 = Rang 1
	 * @throws NotAllResultsException Es fehlen noch Resultate um die Rangliste zu erstellen
	 */
	public Map<Category, List<PlayerCard>> calculateRanking(Tournament tournament, IProgressMonitor pMonitor) throws NotAllResultsException;
	
	/**
	 * Berechnen der Rangliste in einer Kategorie
	 * @param category Kategorie zu der die Rangliste erstellt werden soll.
	 * @param pMonitor ProgrssMonitor
	 * @return Liste mit den Spielern Index 0 = Rang 1
	 * @throws NotAllResultsException Es fehlen noch Resultate um die Rangliste zu erstellen
	 */
	public List<PlayerCard> calculateRanking(Category category, IProgressMonitor pMonitor) throws NotAllResultsException;

}
