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
	 * Ausführen der Paarungen (alle Kategorien)
	 */
	public void executePairings();
	
	/**
	 * Ausführen der Paarungen einer einzelnen Kategorie
	 * @param category
	 */
	public void executePairings(Category category);
}
