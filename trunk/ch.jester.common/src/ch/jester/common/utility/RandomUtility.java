package ch.jester.common.utility;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Utility Klasse um das erzeugen von Zufallszahlen zu vereinfachen.
 *
 */
public class RandomUtility {

	/**
	 * Die Methode erzeugt eine zufällige Reihenfolge der Startnummern.
	 * 
	 * @param numberOfPlayers Anzahl Spieler
	 * @return	Liste mit den Startnummern
	 */
	public static List<Integer> createStartingNumbers(int numberOfPlayers) {
		List<Integer> startingNumbers = new ArrayList<Integer>();
		List<Integer> numbers = new ArrayList<Integer>();
		Random rdm = new Random();
		// Erzeugen der 
		for (int i = 1; i < numberOfPlayers+1; i++) {
			numbers.add(i);
		}
		while (!numbers.isEmpty() && startingNumbers.size() < numberOfPlayers) {
			int rdmIndex = rdm.nextInt(numbers.size());
			startingNumbers.add(numbers.remove(rdmIndex));
		}
		return startingNumbers;
	}
}
