package ch.jester.system.vollrundig;

import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;

import ch.jester.model.Category;
import ch.jester.model.Pairing;
import ch.jester.model.Round;
import ch.jester.model.Tournament;
import ch.jester.system.api.pairing.IPairingAlgorithm;
import ch.jester.system.exceptions.NotAllResultsException;

public class VollrundigPairingAlgorithm implements IPairingAlgorithm {

	private Category category;
	
	@Override
	public List<Pairing> executePairings(Tournament tournament, IProgressMonitor pMonitor) throws NotAllResultsException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Pairing> executePairings(Category category, IProgressMonitor pMonitor) throws NotAllResultsException {
		this.category = category;
		checkResults();
		return null;
	}

	/**
	 * Überprüfen, ob alle Resultate der letzten Runde erfasst wurden
	 * @throws NotAllResultsException
	 */
	private void checkResults() throws NotAllResultsException {
		List<Round> rounds = category.getRounds();
		Round lastRound = rounds.get(rounds.size()-1);
		for (Pairing pairing : lastRound.getPairings()) {
			if (pairing.getResult() == null) throw new NotAllResultsException("Not yet implemented!");
		}
	}

}
