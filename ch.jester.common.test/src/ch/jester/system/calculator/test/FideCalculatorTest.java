package ch.jester.system.calculator.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import ch.jester.common.test.internal.ActivatorProviderForTestCase;
import ch.jester.commonservices.util.ServiceUtility;
import ch.jester.system.api.calculator.IEloCalculator;
import ch.jester.system.api.calculator.IEloCalculatorEntry;
import ch.jester.system.api.calculator.IEloCalculatorManager;

public class FideCalculatorTest extends ActivatorProviderForTestCase {

	private ServiceUtility mServiceUtil = new ServiceUtility(); 
	
	@Test
	public void testGetFideEloCalculatorService() {
		IEloCalculatorManager eloCalcManager = mServiceUtil.getService(IEloCalculatorManager.class);
		assertNotNull("EloCalculator is null", eloCalcManager);
		
		assertNotNull(getFideEloCalculatorService());
	}
	
	@Test
	public void testCalculateEloOneGame() {
		IEloCalculator fideCalculator = getFideEloCalculatorService();
		assertEquals(1714, fideCalculator.calculateElo(1720, 15, 1800, 0));
		assertEquals(1722, fideCalculator.calculateElo(1720, 15, 1800, 0.5));
		assertEquals(1729, fideCalculator.calculateElo(1720, 15, 1800, 1));
	}
	
	@Test
	public void testCalculateManyGames() {
		IEloCalculator fideCalculator = getFideEloCalculatorService();
		List<Integer> opponentRatings = new ArrayList<Integer>();
		opponentRatings.add(1850);
		opponentRatings.add(1900);
		opponentRatings.add(1700);
		opponentRatings.add(1750);
		opponentRatings.add(1850);
		opponentRatings.add(1690);
		opponentRatings.add(1770);
		List<Double> results = new ArrayList<Double>();
		results.add(1.0);
		results.add(0.0);
		results.add(1.0);
		results.add(0.5);
		results.add(0.0);
		results.add(1.0);
		results.add(0.5);
		assertEquals(1737, fideCalculator.calculateElo(1720, 15, opponentRatings, results));
	}
	
	@Test
	public void testCalculateOpponentMean() {
		IEloCalculator fideCalculator = getFideEloCalculatorService();
		ArrayList<Integer> opponentElos = new ArrayList<Integer>();
		opponentElos.add(1720);
		opponentElos.add(1820);
		opponentElos.add(1556);
		opponentElos.add(1903);
		assertEquals(1749.75, fideCalculator.meanOpposites(opponentElos), 0.001);
	}
	
	@Test
	public void testCalculatePerformance() {
		IEloCalculator fideCalculator = getFideEloCalculatorService();
		List<Integer> opponentRatings = new ArrayList<Integer>();
		opponentRatings.add(1850);
		opponentRatings.add(1900);
		opponentRatings.add(1700);
		opponentRatings.add(1750);
		opponentRatings.add(1850);
		opponentRatings.add(1690);
		opponentRatings.add(1770);
		List<Double> results = new ArrayList<Double>();
		results.add(1.0);
		results.add(0.0);
		results.add(1.0);
		results.add(0.5);
		results.add(0.0);
		results.add(1.0);
		results.add(0.5);
		assertEquals(1844, fideCalculator.calculatePerformance(1720, opponentRatings, results));
	}
	
	/**
	 * Sucht nach dem FideEloCalculator
	 * @return
	 */
	private IEloCalculator getFideEloCalculatorService() {
		IEloCalculatorManager eloCalcManager = mServiceUtil.getService(IEloCalculatorManager.class);
		
		List<IEloCalculatorEntry> registredEntries = eloCalcManager.getRegistredEntries();
		for (IEloCalculatorEntry iEloCalculatorEntry : registredEntries) {
			if (iEloCalculatorEntry.getShortType().contains("FIDE")) {
				return iEloCalculatorEntry.getService();
			}
		}
		return null;
	}
}
