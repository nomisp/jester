package ch.jester.system.pairing.impl;

import java.util.List;

import javax.persistence.EntityManager;

import ch.jester.common.utility.RandomUtility;
import ch.jester.commonservices.api.persistency.IDaoService;
import ch.jester.commonservices.util.ServiceUtility;
import ch.jester.model.Category;
import ch.jester.model.Player;
import ch.jester.model.PlayerCard;
import ch.jester.model.Round;
import ch.jester.model.factories.ModelFactory;
import ch.jester.orm.ORMPlugin;
import ch.jester.system.exceptions.NoStartingNumbersException;

/**
 * Hilfsklasse für spezielle Funktionen welche beim Auslosen der Paarungen gebraucht werden können.
 */
public class PairingHelper {

	private static ServiceUtility mServiceUtil = new ServiceUtility();
	private static EntityManager em = ORMPlugin.getJPAEntityManager();
	
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

	/**
	 * Liefert den Dummy-Spieler (FirstName=Dummy, LastName=Dummy)
	 * Falls der Dummy-Spieler nicht existiert wird er angelegt und in der DB gespeichert.
	 * @return
	 */
	public static Player getDummyPlayer() {
		IDaoService<Player> playerPersister = mServiceUtil.getDaoServiceByEntity(Player.class);
		Player dummy = (Player)playerPersister.createNamedQuery(Player.QUERY_DUMMYPLAYER).getSingleResult();
		if (dummy == null) {
			dummy = ModelFactory.getInstance().createPlayer("Dummy", "Dummy");
			playerPersister.save(dummy);
		}
		return dummy;
	}

	/**
	 * Liefert eine Liste mit allen noch offenen Runden in einer Kategorie
	 * @param category
	 * @return
	 */
	public static List<Round> getOpenRounds(Category category) {
		em.joinTransaction();
		@SuppressWarnings("unchecked")
		List<Round> openRounds = em.createNamedQuery("OpenRoundsByCategory")
										.setParameter("category", category)
										.getResultList();
		return openRounds;
	}

	/**
	 * Liefert eine Liste mit allen beendeten Runden in einer Kategorie
	 * @param category
	 * @return
	 */
	public static List<Round> getFinishedRounds(Category category) {
		em.joinTransaction();
		@SuppressWarnings("unchecked")
		List<Round> finishedRounds = em.createNamedQuery("FinishedRoundsByCategory")
										.setParameter("category", category)
										.getResultList();
		return finishedRounds;
	}
}
