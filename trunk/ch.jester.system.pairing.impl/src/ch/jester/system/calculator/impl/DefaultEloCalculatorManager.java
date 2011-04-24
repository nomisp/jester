package ch.jester.system.calculator.impl;

import ch.jester.common.components.AbstractEPComponent;
import ch.jester.system.api.calculator.IEloCalculator;
import ch.jester.system.api.calculator.IEloCalculatorEntry;
import ch.jester.system.api.calculator.IEloCalculatorManager;
import ch.jester.system.internal.SystemActivator;

public class DefaultEloCalculatorManager extends AbstractEPComponent<IEloCalculatorEntry, IEloCalculator> implements IEloCalculatorManager {
	
	public DefaultEloCalculatorManager() {
		super(IEloCalculator.class, 
				SystemActivator.getInstance().getActivationContext(), 
				"ch.jester.system.api", 
				"EloCalculator");		
	}

	@Override
	protected IEloCalculatorEntry createEntry(IEloCalculator o) {
		return new DefaultEloCalculatorEntry(o);
	}

	@Override
	public void doCalculatePerformances() {
		// TODO Auto-generated method stub
		
	}
}
