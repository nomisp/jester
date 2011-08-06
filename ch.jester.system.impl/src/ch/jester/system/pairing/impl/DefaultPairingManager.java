package ch.jester.system.pairing.impl;

import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;

import ch.jester.common.components.AbstractEPComponent;
import ch.jester.model.Category;
import ch.jester.model.Pairing;
import ch.jester.model.Tournament;
import ch.jester.system.api.pairing.IPairingAlgorithm;
import ch.jester.system.api.pairing.IPairingAlgorithmEntry;
import ch.jester.system.api.pairing.IPairingManager;
import ch.jester.system.exceptions.NoPlayersException;
import ch.jester.system.exceptions.NoRoundsException;
import ch.jester.system.exceptions.NotAllResultsException;
import ch.jester.system.exceptions.PairingNotPossibleException;
import ch.jester.system.exceptions.TournamentFinishedException;
import ch.jester.system.internal.SystemActivator;

/**
 * Standardimplementation eines PairingManager
 *
 */
public class DefaultPairingManager extends AbstractEPComponent<IPairingAlgorithmEntry, IPairingAlgorithm> implements IPairingManager {
	
	public DefaultPairingManager() {
		super(IPairingAlgorithm.class, 
				SystemActivator.getInstance().getActivationContext(), 
				"ch.jester.system.api", 
				"PairingAlgorithm");		
	}

	@Override
	public List<Pairing> doPairings(Tournament tournament, IPairingAlgorithmEntry pairingAlgorithm,
			IProgressMonitor progressMonitor) throws NotAllResultsException, PairingNotPossibleException, TournamentFinishedException, NoRoundsException, NoPlayersException {
		return pairingAlgorithm.getService().executePairings(tournament);
	}

	@Override
	public List<Pairing> doPairings(Category category, IPairingAlgorithmEntry pairingAlgorithm,
			IProgressMonitor progressMonitor) throws PairingNotPossibleException, NotAllResultsException, TournamentFinishedException, NoRoundsException, NoPlayersException {
		return pairingAlgorithm.getService().executePairings(category);
	}

	@Override
	protected IPairingAlgorithmEntry createEntry(IPairingAlgorithm o) {
		return new DefaultPairingAlgorithmEntry(o);
	}
}
