package ch.jester.system.calculator.impl;

import ch.jester.common.components.EPEntry;
import ch.jester.system.api.calculator.IEloCalculator;
import ch.jester.system.api.calculator.IEloCalculatorEntry;
import ch.jester.system.api.pairing.IPairingAlgorithmEntry;

public class DefaultEloCalculatorEntry extends EPEntry<IEloCalculator> implements IEloCalculatorEntry {

	public DefaultEloCalculatorEntry(IEloCalculator pService) {
		super(pService);
	}
	
	@Override
	public String getImplementationClass() {
		return getProperty(IPairingAlgorithmEntry.CLASS);
	}

	@Override
	public String getShortType() {
		return getProperty(IEloCalculatorEntry.SHORTTYPE);
	}

	@Override
	public String getDescription() {
		return getProperty(IEloCalculatorEntry.TYPEDESCRIPTION);
	}
	
	public String toString() {
		return !getDescription().isEmpty() ? getDescription() : getShortType();
	}

}
