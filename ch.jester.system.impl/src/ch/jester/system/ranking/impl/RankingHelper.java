package ch.jester.system.ranking.impl;

import java.util.List;

import javax.persistence.EntityManager;

import ch.jester.commonservices.util.ServiceUtility;
import ch.jester.model.Category;
import ch.jester.model.PlayerCard;
import ch.jester.model.Round;
import ch.jester.orm.ORMPlugin;
import ch.jester.system.exceptions.NotAllResultsException;

public class RankingHelper {
	
	private static ServiceUtility mServiceUtil = new ServiceUtility();
	private static EntityManager em = ORMPlugin.getJPAEntityManager();

	/**
	 * Sucht die letzte fertig gespielte Runde ohne Rangliste in einer Kategorie.
	 * @param category
	 * @return
	 */
	public static Round getLastFinishedRound(Category category) {
//		EntityManager em = ORMPlugin.getJPAEntityManager();
		em.joinTransaction();
		@SuppressWarnings("unchecked")
		List<Round> finishedRounds = em.createNamedQuery("FinishedRoundsByCategory")
										.setParameter("category", category)
										.getResultList();
		if (finishedRounds.size() > 0) {
			Round lastFinishedRound = finishedRounds.get(0);
			for (Round round : finishedRounds) {
				if (round.getNumber() > lastFinishedRound.getNumber()) {
					lastFinishedRound = round;
				}
			}
			return lastFinishedRound;
		}
		return null;
	}

	/**
	 * Liefert eine Liste mit allen beendeten Runden in einer Kategorie
	 * @param category
	 * @return
	 */
	public static List<Round> getFinishedRounds(Category category) {
//		EntityManager em = ORMPlugin.getJPAEntityManager();
		em.joinTransaction();
		@SuppressWarnings("unchecked")
		List<Round> finishedRounds = em.createNamedQuery("FinishedRoundsByCategory")
										.setParameter("category", category)
										.getResultList();
		return finishedRounds;
	}
	
	/**
	 * Überprüfen ob alle Resultate in einer Kategorie erfasst wurden.
	 * Keine offene Runden mehr.
	 * @param category
	 * @return true wenn alle Runden beendet (alle Resultate erfasst) sind.
	 */
	public static boolean allResultsAvailable(Category category) {
//		em = ORMPlugin.getJPAEntityManager();
		@SuppressWarnings("unchecked")
		List<Round> openRounds = em.createNamedQuery("OpenRounds").getResultList();
		return openRounds.size() == 0;
	}
	
	/**
	 * Erzeugen einer initialen Rangliste anhand der Punkte.
	 * Noch ohne Einbezug der Feinwertung
	 * @param category
	 * @return
	 * @throws NotAllResultsException 
	 */
	public static List<PlayerCard> getInitialIntermediateRanking(Category category) throws NotAllResultsException {
		em.joinTransaction();
		List<Round> finishedRounds = RankingHelper.getFinishedRounds(category);
		if (finishedRounds == null) {
			throw new NotAllResultsException();
		}
		@SuppressWarnings("unchecked")
		List<PlayerCard> ranking = em.createNamedQuery("PlayerCardsByCategoryAndFinishedRoundsOrderByPoints")
											.setParameter("category", category)
											.setParameter("finishedRounds", finishedRounds)
											.getResultList();
		for (PlayerCard playerCard : ranking) {
			System.out.println(playerCard.getPlayer() + " : " + playerCard.getPoints());
		}
		return ranking;
	}

	/**
	 * Erzeugen einer initialen Rangliste anhand der Punkte.
	 * Noch ohne Einbezug der Feinwertung
	 * @param category
	 * @return
	 */
	public static List<PlayerCard> getInitialFinalRanking(Category category) {
		em.joinTransaction();
		@SuppressWarnings("unchecked")
		List<PlayerCard> ranking = em.createNamedQuery("PlayerCardsByCategoryOrderByPoints")
											.setParameter("category", category)
											.getResultList();
//		for (PlayerCard playerCard : ranking) {
//			System.out.println(playerCard.getPlayer() + " : " + playerCard.getPoints());
//		}
		return ranking;
	}
}
