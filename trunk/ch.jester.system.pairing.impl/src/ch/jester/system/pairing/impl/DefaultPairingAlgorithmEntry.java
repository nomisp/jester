package ch.jester.system.pairing.impl;

import ch.jester.common.components.EPEntry;
import ch.jester.system.api.pairing.IPairingAlgorithm;
import ch.jester.system.api.pairing.IPairingAlgorithmEntry;

public class DefaultPairingAlgorithmEntry extends EPEntry<IPairingAlgorithm> implements IPairingAlgorithmEntry {

	public DefaultPairingAlgorithmEntry(IPairingAlgorithm pService) {
		super(pService);
	}

	@Override
	public String getShortType() {
		return getProperty(IPairingAlgorithmEntry.SHORTTYPE);
	}

	@Override
	public String getDescription() {
		return getProperty(IPairingAlgorithmEntry.TYPEDESCRIPTION);
	}
	
	public String toString() {
		return !getDescription().isEmpty() ? getDescription() : getShortType();
	}

}
