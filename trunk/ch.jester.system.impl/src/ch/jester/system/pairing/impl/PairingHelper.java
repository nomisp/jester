package ch.jester.system.pairing.impl;

import java.util.ArrayList;
import java.util.List;

import ch.jester.common.utility.RandomUtility;
import ch.jester.model.PlayerCard;
import ch.jester.system.exceptions.NoStartingNumbersException;

/**
 * Hilfsklasse für spezielle Funktionen welche beim Auslosen der Paarungen gebraucht werden können.
 * @author Peter
 *
 */
public class PairingHelper {

	/**
	 * Sortiert die Liste mit den PlayerCards anhand der Startnummer
	 * @param playerCards
	 * @return	Sortierte Liste mit den PlayerCards (als Array)
	 * @throws NoStartingNumbersException 
	 */
	public static PlayerCard[] getOrderedPlayerCards(List<PlayerCard> playerCards) throws NoStartingNumbersException {
		PlayerCard[] orderedList = new PlayerCard[playerCards.size()];
		Integer number;
		for (PlayerCard playerCard : playerCards) {
			number = playerCard.getNumber();
			if (number == null || number == 0) throw new NoStartingNumbersException();
			orderedList[number-1] = playerCard;
		}
		return orderedList;
	}
	
	/**
	 * Delegate auf die entsprechende Methode der Klasse RandomUtility im ch.jester.common-Plugin
	 * @param numberOfPlayers
	 * @return Liste mit den Startnummern
	 */
	public static List<Integer> createRandomStartingNumbers(int numberOfPlayers) {
		return RandomUtility.createStartingNumbers(numberOfPlayers);
	}
}
