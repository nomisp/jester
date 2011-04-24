package ch.jester.system.api.pairing;

import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;

import ch.jester.commonservices.api.components.IEPEntryComponentService;
import ch.jester.model.Category;
import ch.jester.model.Pairing;
import ch.jester.system.exceptions.NotAllResultsException;

public interface IPairingManager extends IEPEntryComponentService<IPairingAlgorithmEntry, IPairingAlgorithm> {
	
	/**
	 * Ausf�hren der Paarungen mit dem �bergebenen Paarungsalgorithmus
	 * @param pairingAlgorithm
	 * @param progressMonitor
	 * @return Liste mit den Paarungen Index 0 = Brett 1
	 * @throws NotAllResultsException Es fehlen noch Resultate um die Rangliste zu erstellen
	 */
	public List<Pairing> doPairings(IPairingAlgorithmEntry pairingAlgorithm, IProgressMonitor progressMonitor) throws NotAllResultsException;

	/**
	 * Ausf�hren der Paarungen innerhalb einer Kategorie
	 * mit dem �bergebenen Paarungsalgorithmus
	 * @param category
	 * @param pairingAlgorithm
	 * @param progressMonitor
	 * @return Liste mit den Paarungen Index 0 = Brett 1
	 * @throws NotAllResultsException Es fehlen noch Resultate um die Rangliste zu erstellen
	 */
	public List<Pairing> doPairings(Category category, IPairingAlgorithmEntry pairingAlgorithm, IProgressMonitor progressMonitor) throws NotAllResultsException;
}
