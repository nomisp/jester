package ch.jester.system.api.ranking;

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
	 */
	public void calculateRanking();
}
