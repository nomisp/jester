package ch.jester.system.api.calculator;

import ch.jester.commonservices.api.components.IEPEntryComponentService;

public interface IEloCalculatorManager extends IEPEntryComponentService<IEloCalculatorEntry, IEloCalculator> {

	/**
	 * Berechnen der Performance aller Spieler
	 */
	public void doCalculatePerformances();
}
