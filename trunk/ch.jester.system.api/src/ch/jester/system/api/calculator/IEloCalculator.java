package ch.jester.system.api.calculator;

import java.util.List;

/**
 * Wertungszahl Rechner
 *
 */
public interface IEloCalculator {
	
	/**
	 * Berechnen der neuen Elo (oder NWZ)
	 * 
	 * @param actual		Aktuelle Wertung (bleibt über eine Wertungsperiode unverändert)
	 * @param coeff			Entwicklungskoeffizient
	 * @param oppositeElo	gegnerische Wertungszahl
	 * @param result		Resultat (0=verloren; 1=gewonnen; 0.5=remis)
	 * @return	neue Wertungszahl
	 */
	public int calculateElo(int actual, int coeff, int oppositeElo, double result);
	
	/**
	 * Berechnen der neuen Elo (oder NWZ)
	 * 
	 * @param actual		Aktuelle Wertung (bleibt über eine Wertungsperiode unverändert)
	 * @param coeff			Entwicklungskoeffizient
	 * @param oppositeElos	Liste mit den gegnerischen Elos (innerhalb der Wertungsperiode)
	 * @param results		Liste mit den erzielten Resultaten (muss zu oppositeElos korrelieren) 
	 * @return	neue Wertungszahl
	 */
	public int calculateElo(int actual, int coeff, List<Integer> oppositeElos, List<Double> results);
	
	/**
	 * Berechnen der Performance ohne den Entwicklungskoeffizienten miteinzubeziehen.
	 * 
	 * @param actual		Aktuelle Wertung (bleibt über eine Wertungsperiode unverändert)
	 * @param oppositeElos	Liste mit den gegnerischen Elos (innerhalb der Wertungsperiode)
	 * @param results		Liste mit den erzielten Resultaten (muss zu oppositeElos korrelieren) 
	 * @return	neue Wertungszahl
	 */
	public int calculatePerformance(int actual, List<Integer> oppositeElos, List<Double> results);
	
	/**
	 * Berechnen der Exakten Performance
	 * 
	 * @param actual		Aktuelle Wertung (bleibt über eine Wertungsperiode unverändert)
	 * @param coeff			Entwicklungskoeffizient
	 * @param oppositeElos	Liste mit den gegnerischen Elos (innerhalb der Wertungsperiode)
	 * @param results		Liste mit den erzielten Resultaten (muss zu oppositeElos korrelieren) 
	 * @return	neue Wertungszahl
	 */
	public int calculateExactPerformance(int actual, int coeff, List<Integer> oppositeElos, List<Double> results);
	
	/**
	 * Berechnen des Durchschnitts der gegnerischen Wertungszahlen
	 * @param oppositeElos	Liste mit den gegnerischen Elos
	 * @return	Gegnerschnitt
	 */
	public double meanOpposites(List<Integer> oppositeElos);
	
	/**
	 * Berechnen der Gewinnwahrscheinlichkeit
	 * @param playerElo
	 * @param opponentElo
	 * @return Gewinnwahrscheinlichkeit
	 */
	public double calculateScoreProbability(int playerElo, int opponentElo);
}
