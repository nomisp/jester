package ch.jester.system.ranking.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.persistence.EntityManager;

import ch.jester.model.Category;
import ch.jester.model.Pairing;
import ch.jester.model.PlayerCard;
import ch.jester.model.Ranking;
import ch.jester.model.RankingEntry;
import ch.jester.model.RankingSystemPoint;
import ch.jester.model.Round;
import ch.jester.model.factories.ModelFactory;
import ch.jester.orm.ORMPlugin;
import ch.jester.system.exceptions.NotAllResultsException;

/**
 * Hilfsklasse für das Erstellen von Ranglisten
 *
 */
public class RankingHelper {
	
	private static EntityManager em = ORMPlugin.getJPAEntityManager();

	/**
	 * Sucht die letzte fertig gespielte Runde ohne Rangliste in einer Kategorie.
	 * @param category
	 * @return
	 */
	public static Round getLastFinishedRound(Category category) {
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
		@SuppressWarnings("unchecked")
		List<Round> openRounds = em.createNamedQuery("OpenRoundsByCategory")
									.setParameter("category", category)
									.getResultList();
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
//		for (PlayerCard playerCard : ranking) {
//			System.out.println(playerCard.getPlayer() + " : " + playerCard.getPoints());
//		}
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
	
	/**
	 * Liefert eine Liste mit allen Paarungen eines Spielers innerhalb einer Kategorie
	 * @param player
	 * @param category
	 * @return
	 */
	public static List<Pairing> getPlayerPairings(PlayerCard player, Category category) {
		em.joinTransaction();
		@SuppressWarnings("unchecked")
		List<Pairing> pairings = em.createNamedQuery("PairingsByPlayerCardAndCategory")
											.setParameter("player", player)
											.setParameter("category", category)
											.getResultList();
		return pairings;
	}
	
	/**
	 * Liefert eine Liste mit den Gegnern eines Spielers
	 * innerhalb der gegebenen Runden.
	 * @param player
	 * @param rounds Runden, welche für die Rangliste berücksichtigt werden
	 * @return
	 */
	public static List<PlayerCard> getOpponents(PlayerCard player, List<Round> rounds) {
		List<PlayerCard> opponents = new ArrayList<PlayerCard>();
		for (Round round : rounds) {
			for (Pairing pairing : round.getPairings()) {
				if (pairing.getWhite().equals(player)) {
					opponents.add(pairing.getBlack());
				} else if (pairing.getBlack().equals(player)) {
					opponents.add(pairing.getWhite());
				}
			}
		}
		return opponents;
	}
	
	/**
	 * Liefert eine Liste mit den Gegnern, gegen die gewonnen wurde
	 * @param player
	 * @param rounds Runden, welche für die Rangliste berücksichtigt werden
	 * @return
	 */
	public static List<PlayerCard> getWins(PlayerCard player, List<Round> rounds) {
		List<PlayerCard> opponents = new ArrayList<PlayerCard>();
		for (Round round : rounds) {
			for (Pairing pairing : round.getPairings()) {
				if (pairing.getWhite().equals(player) && pairing.getResult().startsWith("1")) {
					opponents.add(pairing.getBlack());
				} else if (pairing.getBlack().equals(player) && pairing.getResult().startsWith("0")) {
					opponents.add(pairing.getWhite());
				}
			}
		}
		return opponents;
	}
	
	/**
	 * Liefert eine Liste mit den Gegnern, gegen die verloren wurde
	 * @param player
	 * @param rounds Runden, welche für die Rangliste berücksichtigt werden
	 * @return
	 */
	public static List<PlayerCard> getLosts(PlayerCard player, List<Round> rounds) {
		List<PlayerCard> opponents = new ArrayList<PlayerCard>();
		for (Round round : rounds) {
			for (Pairing pairing : round.getPairings()) {
				if (pairing.getWhite().equals(player) && pairing.getResult().startsWith("0")) {
					opponents.add(pairing.getBlack());
				} else if (pairing.getBlack().equals(player) && pairing.getResult().startsWith("1")) {
					opponents.add(pairing.getWhite());
				}
			}
		}
		return opponents;
	}
	
	/**
	 * Liefert eine Liste mit den Gegnern, gegen die remisiert wurde
	 * @param player
	 * @param rounds Runden, welche für die Rangliste berücksichtigt werden
	 * @return
	 */
	public static List<PlayerCard> getRemis(PlayerCard player, List<Round> rounds) {
		List<PlayerCard> opponents = new ArrayList<PlayerCard>();
		for (Round round : rounds) {
			for (Pairing pairing : round.getPairings()) {
				if (pairing.getWhite().equals(player) && pairing.getResult().equalsIgnoreCase("x")) {
					opponents.add(pairing.getBlack());
				} else if (pairing.getBlack().equals(player) && pairing.getResult().equalsIgnoreCase("x")) {
					opponents.add(pairing.getWhite());
				}
			}
		}
		return opponents;
	}

	/**
	 * Erzeugen des Rankings anhand der Liste mit den PlayerCards
	 * @param ranking Entweder ein FinalRanking oder ein IntermediateRanking
	 * @param players	Liste mit den PlayerCards welche bereits alle einen entsprechenden RankingSystemPoint haben.<br/>
	 * 					<b>Die Liste muss nach Punkten vorsortiert sein!</b><br/>
	 * 					<code>RankingHelper.getInitialFinalRanking()</code> oder 
	 * 					<code>RankingHelper.getInitialIntermediateRanking()</code>
	 * @param rankingSystem RankingSystem-shortType des gewünschten RankingSystems (wie er im RankingSystemPoint definiert ist)
	 */
	public static void createRanking(Ranking ranking, List<PlayerCard> players, final String rankingSystem) {
		//List<RankingSystemPoint> rankingSystemPoints = new ArrayList<RankingSystemPoint>(players.size());
		ModelFactory modelFactory = ModelFactory.getInstance();
/*		for (PlayerCard playerCard : players) {
			rankingSystemPoints.add(playerCard.getRankingSystemPoint(rankingSystem)); // TODO Peter: evtl. null check und Exception schmeissen
		}*/
		//TODO: Von Matthias: Peter bitte checken
	/*	Collections.sort(rankingSystemPoints, new Comparator<RankingSystemPoint>() {

			@Override
			public int compare(RankingSystemPoint o1, RankingSystemPoint o2) {
				if(o1.getPoints().doubleValue()==o2.getPoints().doubleValue()){
					return 0;
				}
				if(o1.getPoints().doubleValue()>o2.getPoints().doubleValue()){
					return -1;
				}
				return 1;
			}
			
		});*/
		//TODO: Von Matthias: Peter bitte checken
		Collections.sort(players, new Comparator<PlayerCard>() {

			public int compare(RankingSystemPoint o1, RankingSystemPoint o2) {
				if(o1.getPoints().doubleValue()==o2.getPoints().doubleValue()){
					return 0;
				}
				if(o1.getPoints().doubleValue()>o2.getPoints().doubleValue()){
					return -1;
				}
				return 1;
			}
			@Override
			public int compare(PlayerCard o1, PlayerCard o2) {
				if(o1.getPoints().doubleValue()>o2.getPoints().doubleValue()){
					return -1;
				}else if(o1.getPoints().doubleValue()<o2.getPoints().doubleValue()){
					return 1;
				}else{
					RankingSystemPoint rsp1 =o1.getRankingSystemPoint(rankingSystem);
					RankingSystemPoint rsp2 =o2.getRankingSystemPoint(rankingSystem);
					return compare(rsp1, rsp2);
				}
			}
		
		
		});
		
		for(int i=0;i<players.size();i++){
			RankingEntry rankingEntry = modelFactory.createRankingEntry(players.get(i));
			rankingEntry.setPosition(i+1);
			ranking.addRankingEntry(rankingEntry);
			
		}
		/*for (int i = 0; i < rankingSystemPoints.size(); i++) {
			RankingSystemPoint rankingSystemPoint = rankingSystemPoints.get(i);
			RankingEntry rankingEntry = modelFactory.createRankingEntry(rankingSystemPoint.getPlayerCard());
			
			rankingEntry.setPosition(i+1);
			ranking.addRankingEntry(rankingEntry);
		}*/
	}
	
	/**
	 * Zu debugzwecken:
	 * 
	 */
	public static void printRanking(Ranking ranking) {
		for (RankingEntry pos : ranking.getRankingEntries()) {
			System.out.println(pos);
		}
	}
}
