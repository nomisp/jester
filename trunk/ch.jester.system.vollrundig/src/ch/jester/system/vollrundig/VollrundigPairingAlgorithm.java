package ch.jester.system.vollrundig;

import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;

import ch.jester.model.Category;
import ch.jester.model.Pairing;
import ch.jester.model.Round;
import ch.jester.system.api.pairing.IPairingAlgorithm;
import ch.jester.system.exceptions.NotAllResultsException;

public class VollrundigPairingAlgorithm implements IPairingAlgorithm {

	private Category category;
	
	@Override
	public List<Pairing> executePairings(IProgressMonitor pMonitor) throws NotAllResultsException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Pairing> executePairings(Category category, IProgressMonitor pMonitor) throws NotAllResultsException {
		this.category = category;
		checkResults();
		return null;
	}

	private void checkResults() throws NotAllResultsException {
		Round lastRound = null;;
		for (Round round : category.getRounds()) {
			lastRound = round;
		}
		for (Pairing pairing : lastRound.getPairings()) {
			if (pairing.getResult() == null) throw new NotAllResultsException("Not yet implemented!");
		}
	}

}
