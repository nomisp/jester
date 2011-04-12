package ch.jester.system.api.ranking;

/**
 * Interface f�r die Feinwertungen zur Berechnung
 * der Rangliste. Die verschiedenen Feinwertungen 
 * m�ssen dieses Interface implementieren.
 * @author Peter
 *
 */
public interface IRankingSystem {

	/**
	 * Berechnen der Rangliste
	 */
	public void calculateRanking();
}
