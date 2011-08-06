package ch.jester.system.api.calculator;

import ch.jester.commonservices.api.components.IEPEntryComponentService;

/**
 * Schnittstelle für einen Elo-/Performance Rechner
 *
 */
public interface IEloCalculatorManager extends IEPEntryComponentService<IEloCalculatorEntry, IEloCalculator> {

	/**
	 * Berechnen der Performance aller Spieler
	 */
	public void doCalculatePerformances();
}
