package ch.jester.system.api.pairing;

import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;

import ch.jester.commonservices.api.components.IEPEntryComponentService;
import ch.jester.model.Category;
import ch.jester.model.Pairing;
import ch.jester.model.Tournament;
import ch.jester.system.exceptions.NotAllResultsException;
import ch.jester.system.exceptions.PairingNotPossibleException;
import ch.jester.system.exceptions.TournamentFinishedException;

public interface IPairingManager extends IEPEntryComponentService<IPairingAlgorithmEntry, IPairingAlgorithm> {
	
	/**
	 * Ausführen der Paarungen mit dem übergebenen Paarungsalgorithmus
	 * @param tournament
	 * @param pairingAlgorithm
	 * @param progressMonitor
	 * @return Liste mit den Paarungen Index 0 = Brett 1
	 * @throws NotAllResultsException Es fehlen noch Resultate um die Rangliste zu erstellen
	 * @throws PairingNotPossibleException Paarungsauslosung kann nicht durchgeführt werden z.B. sind weder Runden noch Spieler zugewiesen
	 * @throws TournamentFinishedException 
	 */
	public List<Pairing> doPairings(Tournament tournament, IPairingAlgorithmEntry pairingAlgorithm, IProgressMonitor progressMonitor) throws NotAllResultsException, PairingNotPossibleException, TournamentFinishedException;

	/**
	 * Ausführen der Paarungen innerhalb einer Kategorie
	 * mit dem übergebenen Paarungsalgorithmus
	 * @param category
	 * @param pairingAlgorithm
	 * @param progressMonitor
	 * @return Liste mit den Paarungen Index 0 = Brett 1
	 * @throws NotAllResultsException Es fehlen noch Resultate um die Rangliste zu erstellen
	 * @throws PairingNotPossibleException Paarungsauslosung kann nicht durchgeführt werden z.B. sind weder Runden noch Spieler zugewiesen
	 * @throws TournamentFinishedException 
	 */
	public List<Pairing> doPairings(Category category, IPairingAlgorithmEntry pairingAlgorithm, IProgressMonitor progressMonitor) throws NotAllResultsException, PairingNotPossibleException, TournamentFinishedException;
}
