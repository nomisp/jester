package ch.jester.system.api.calculator;

import java.util.ArrayList;

public interface IEloCalculator {
	
	/**
	 * Berechnen der neuen Elo (oder NWZ)
	 * 
	 * @param actual		Aktuelle Wertung
	 * @param coeff			Entwicklungskoeffizient
	 * @param oppositeElo	gegnerische Wertungszahl
	 * @param result		Resultat (0=verloren; 1=gewonnen; 2=remis)
	 * @return	neue Wertungszahl
	 */
	public int calculateElo(int actual, int coeff, int oppositeElo, int result);
	
	/**
	 * Berechnen der neuen Elo (oder NWZ)
	 * 
	 * @param actual		Aktuelle Wertung
	 * @param coeff			Entwicklungskoeffizient
	 * @param oppositeElos	Liste mit den gegnerischen Elos (innerhalb der Wertungsperiode)
	 * @param results		Liste mit den erzielten Resultaten (muss zu oppositeElos korrelieren) 
	 * @return	neue Wertungszahl
	 */
	public int calculateElo(int actual, int coeff, ArrayList<Integer> oppositeElos, ArrayList<Integer> results);
	
	/**
	 * Berechnen der Performance
	 * 
	 * @param actual		Aktuelle Wertung
	 * @param coeff			Entwicklungskoeffizient
	 * @param oppositeElos	Liste mit den gegnerischen Elos (innerhalb der Wertungsperiode)
	 * @param results		Liste mit den erzielten Resultaten (muss zu oppositeElos korrelieren) 
	 * @return	neue Wertungszahl
	 */
	public int calculatePerformance(int actual, int coeff, ArrayList<Integer> oppositeElos, ArrayList<Integer> results);
	
	/**
	 * Berechnen des Durchschnitts der gegnerischen Wertungszahlen
	 * @param oppositeElos	Liste mit den gegnerischen Elos
	 * @return	Gegnerschnitt
	 */
	public int meanOpposites(ArrayList<Integer> oppositeElos);
}
