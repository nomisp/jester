package ch.jester.system.api.ranking;

import java.util.List;

import ch.jester.model.Category;
import ch.jester.model.Player;
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
	 * @return Liste mit den Spielern Index 0 = Rang 1
	 * @throws NotAllResultsException Es fehlen noch Resultate um die Rangliste zu erstellen
	 */
	public List<Player> calculateRanking() throws NotAllResultsException;
	
	/**
	 * Berechnen der Rangliste in einer Kategorie
	 * @param category
	 * @return Liste mit den Spielern Index 0 = Rang 1
	 * @throws NotAllResultsException Es fehlen noch Resultate um die Rangliste zu erstellen
	 */
	public List<Player> calculateRanking(Category category) throws NotAllResultsException;
}
