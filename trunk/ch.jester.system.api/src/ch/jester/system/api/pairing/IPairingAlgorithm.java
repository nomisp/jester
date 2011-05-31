package ch.jester.system.api.pairing;

import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;

import ch.jester.model.Category;
import ch.jester.model.Pairing;
import ch.jester.model.Tournament;
import ch.jester.system.exceptions.NotAllResultsException;
import ch.jester.system.exceptions.PairingNotPossibleException;

/**
 * Interface welches von konkreten Paarungsalgorithmen
 * implementiert werden muss.
 * @author Peter
 *
 */
public interface IPairingAlgorithm {

	/**
	 * Ausführen der Paarungen (alle Kategorien)
	 * @param tournament Turnier von welchem die Paarungen gemacht werden sollen
	 * @return Liste mit den Paarungen Index 0 = Brett 1
	 * @throws NotAllResultsException Es fehlen noch Resultate um die Rangliste zu erstellen
	 * @throws PairingNotPossibleException Paarungsauslosung kann nicht durchgeführt werden z.B. sind weder Runden noch Spieler zugewiesen
	 */
	public List<Pairing> executePairings(Tournament tournament, IProgressMonitor pMonitor) throws NotAllResultsException, PairingNotPossibleException;
	
	/**
	 * Ausführen der Paarungen einer einzelnen Kategorie
	 * @param category
	 * @return Liste mit den Paarungen Index 0 = Brett 1
	 * @throws NotAllResultsException Es fehlen noch Resultate um die Rangliste zu erstellen
	 * @throws PairingNotPossibleException Paarungsauslosung kann nicht durchgeführt werden z.B. sind weder Runden noch Spieler zugewiesen
	 */
	public List<Pairing> executePairings(Category category, IProgressMonitor pMonitor) throws NotAllResultsException, PairingNotPossibleException;
}
