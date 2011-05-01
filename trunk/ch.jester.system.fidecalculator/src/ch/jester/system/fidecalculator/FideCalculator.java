package ch.jester.system.fidecalculator;

import java.util.ArrayList;

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
	
	private final int RATING_DIFFERENCE = 400;
	private final double STRONGER_PLAYER = 0.92;
	private final double WEAKER_PLAYER = 0.08;
	
	public FideCalculator() {
	}

	@Override
	public int calculateElo(int actual, int coeff, int oppositeElo, double result) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int calculateElo(int actual, int coeff,
			ArrayList<Integer> oppositeElos, ArrayList<Double> results) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int calculatePerformance(int actual, int coeff,
			ArrayList<Integer> oppositeElos, ArrayList<Double> results) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int meanOpposites(ArrayList<Integer> oppositeElos) {
		// TODO Auto-generated method stub
		return 0;
	}

	/**
	 * @see http://www.fide.com/fide/handbook.html?id=73&view=article Tabelle 8.1(b)
	 */
	@Override
	public double calculateScoreProbability(int playerElo, int opponentElo) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	/**
	 * Berechnete die Elo-Differenz
	 * @param playerRating
	 * @param opponentRating
	 * @return Elo Differenz
	 */
	private int calcEloDifference(int playerRating, int opponentRating) {
		return playerRating-opponentRating;
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
		double res = 0.0;
		
		return res;
	}
}
