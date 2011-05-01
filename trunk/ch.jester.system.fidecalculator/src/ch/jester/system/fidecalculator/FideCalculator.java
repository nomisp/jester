package ch.jester.system.fidecalculator;

import java.util.ArrayList;
import java.util.List;

import ch.jester.system.api.calculator.IEloCalculator;

/**
 * FIDE Elo Rechner
 * Berechnung nach der Formel der FIDE
 * Es werden folgende Entwicklungskoeffizienten (K) verwendet:
 * K = 25 for a player new to the rating list until he has completed events with a total of at least 30 games.
 * K = 15 as long as a player`s rating remains under 2400.
 * K = 10 once a player`s published rating has reached 2400, 
 * and he has also completed events with a total of at least 30 games. Thereafter it remains permanently at 10.
 * 
 * @see http://www.fide.com/fide/handbook.html?id=73&view=article
 * 
 * @author Peter
 *
 */
public class FideCalculator implements IEloCalculator {
	
	private final double RATING_DIFFERENCE = 400.0; // Rating Differenz für FIDE Differenzen > 400 werden wie eine Differenz von 400 berechnet 
	
	public FideCalculator() {
	}

	@Override
	public int calculateElo(int actual, int coeff, int oppositeElo, double result) {
		long newElo = actual + Math.round(calculateEloChange(actual, coeff, oppositeElo, result));
		return (int) newElo;	// TODO peter: Rechner wenn noch keine Elo!
	}

	@Override
	public int calculateElo(int actual, int coeff, ArrayList<Integer> oppositeElos, ArrayList<Double> results) {
		long newElo = actual;
		for (int i = 0; i < oppositeElos.size(); i++) {
			newElo += calculateElo((int)newElo, coeff, oppositeElos.get(i), results.get(i));
		}
		return (int)newElo;
	}

	@Override
	public int calculatePerformance(int actual, int coeff,
			ArrayList<Integer> oppositeElos, ArrayList<Double> results) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double meanOpposites(List<Integer> oppositeElos) {
		double sum = 0;
		for (Integer oppositeElo : oppositeElos) {
			sum += oppositeElo;
		}
		return sum / oppositeElos.size();
	}

	/**
	 * @see http://www.fide.com/fide/handbook.html?id=73&view=article Tabelle 8.1(b)
	 */
	@Override
	public double calculateScoreProbability(int playerElo, int opponentElo) {
		return 1.0 / (Math.pow(10,((opponentElo - playerElo) / RATING_DIFFERENCE)) + 1.0);
	}
	
	/**
	 * Berechnen der Elo-Veränderung einer Partie
	 * @param actual	Aktuelle Elo (bleibt über eine Wertungsperiode unverändert)
	 * @param coeff
	 * @param oppositeElo
	 * @param result
	 * @return
	 */
	private double calculateEloChange(int actual, int coeff, int oppositeElo, double result) {
		double kValue = (double) coeff;
	    double winProb = 1.0 / (Math.pow(10,((oppositeElo - actual) / RATING_DIFFERENCE)) + 1.0);
	    double ratingDiff = Math.round(kValue * (result - winProb));
		
		return ratingDiff;
	}
}
