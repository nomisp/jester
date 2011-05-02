package ch.jester.system.fidecalculator;

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
	public int calculateElo(int actual, int coeff, List<Integer> oppositeElos, List<Double> results) {
		double totEloChange = 0.0;
		for (int i = 0; i < oppositeElos.size(); i++) {
			totEloChange += calculateEloChange(actual, coeff, oppositeElos.get(i), results.get(i));
		}
		return actual + (int)Math.round(totEloChange);
	}

	@Override
	public int calculatePerformance(int actual, List<Integer> oppositeElos, List<Double> results) {
		int sumOpponentRating = calcSumOpponentRating(oppositeElos);
		int[] noResults = calcNoOfResults(results);
		
		double p = ((sumOpponentRating + RATING_DIFFERENCE * (noResults[0] - noResults[1])) / results.size());
		return (int) Math.round(p);
	}

	@Override
	public int calculateExactPerformance(int actual, int coeff, List<Integer> oppositeElos, List<Double> results) {
		// TODO peter: Berechnung der Exakten Performance
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
	    double winProb = calculateWinProbability(actual, oppositeElo);
	    double ratingDiff = Math.round(kValue * (result - winProb));
		
		return ratingDiff;
	}
	
	/**
	 * Berechnen der Gewinnwahrscheinlichkeit
	 * @param actual	Aktuelle Wertungszahl
	 * @param oppositeElo Gegnerische Wertungszahl
	 * @return Gewinnwahrscheinlichkeit
	 */
	private double calculateWinProbability(int actual, int oppositeElo) {
		return 1.0 / (Math.pow(10,((oppositeElo - actual) / RATING_DIFFERENCE)) + 1.0);
	}

	/**
	 * Berechnen der Summe der gegnerischen Elo
	 * @param oppositeElos
	 * @return Summe der gegnerischen Elo
	 */
	private int calcSumOpponentRating(List<Integer> oppositeElos) {
		int sum = 0;
		for (Integer integer : oppositeElos) {
			sum += integer;
		}
		return sum;
	}

	/**
	 * Berechnen der Anzahl Siege, Niederlagen und Remis
	 * @param results Liste mit den Resultaten (1.0=Sieg, 0.0=Niederlage, 0.5=Remis)
	 * @return	Array[0]=Anzahl Siege, Array[1]=Anzahl Niederlagen, Array[2]=Anzahl Remis
	 */
	private int[] calcNoOfResults(List<Double> results) {
		int[] noResults = new int[3];
		int sumWon = 0;
		int sumLost = 0;
		int sumRemis = 0;
		for (Double res : results) {
			if (res == 1.0) sumWon++;
			if (res == 0.0) sumLost++;
			if (res == 0.5) sumRemis++;
		}
		noResults[0] = sumWon;
		noResults[1] = sumLost;
		noResults[2] = sumRemis;
		return noResults;
	}
}
