package ch.jester.system.api.pairing;

import ch.jester.model.Category;

/**
 * Interface welches von konkreten Paarungsalgorithmen
 * implementiert werden muss.
 * @author Peter
 *
 */
public interface IPairingAlgorithm {

	/**
	 * Ausf�hren der Paarungen (alle Kategorien)
	 */
	public void executePairings();
	
	/**
	 * Ausf�hren der Paarungen einer einzelnen Kategorie
	 * @param category
	 */
	public void executePairings(Category category);
}
