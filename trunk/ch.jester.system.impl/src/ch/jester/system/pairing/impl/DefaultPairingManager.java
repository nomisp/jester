package ch.jester.system.pairing.impl;

import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;

import ch.jester.common.components.AbstractEPComponent;
import ch.jester.model.Category;
import ch.jester.model.Pairing;
import ch.jester.system.api.pairing.IPairingAlgorithm;
import ch.jester.system.api.pairing.IPairingAlgorithmEntry;
import ch.jester.system.api.pairing.IPairingManager;
import ch.jester.system.exceptions.NotAllResultsException;
import ch.jester.system.internal.SystemActivator;

public class DefaultPairingManager extends AbstractEPComponent<IPairingAlgorithmEntry, IPairingAlgorithm> implements IPairingManager {
	
	public DefaultPairingManager() {
		super(IPairingAlgorithm.class, 
				SystemActivator.getInstance().getActivationContext(), 
				"ch.jester.system.api", 
				"PairingAlgorithm");		
	}

	@Override
	public List<Pairing> doPairings(IPairingAlgorithmEntry pairingAlgorithm,
			IProgressMonitor progressMonitor) throws NotAllResultsException {
		return pairingAlgorithm.getService().executePairings(progressMonitor);
	}

	@Override
	public List<Pairing> doPairings(Category category,
			IPairingAlgorithmEntry pairingAlgorithm,
			IProgressMonitor progressMonitor) throws NotAllResultsException {
		return pairingAlgorithm.getService().executePairings(category, progressMonitor);
	}

	@Override
	protected IPairingAlgorithmEntry createEntry(IPairingAlgorithm o) {
		return new DefaultPairingAlgorithmEntry(o);
	}
}
