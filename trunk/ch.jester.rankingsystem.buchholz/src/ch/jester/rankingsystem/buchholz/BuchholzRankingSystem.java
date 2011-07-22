package ch.jester.rankingsystem.buchholz;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.IProgressMonitor;

import ch.jester.commonservices.api.components.IEPEntry;
import ch.jester.commonservices.api.components.IEPEntryConfig;
import ch.jester.commonservices.api.persistency.IDaoService;
import ch.jester.commonservices.util.ServiceUtility;
import ch.jester.model.Category;
import ch.jester.model.FinalRanking;
import ch.jester.model.IntermediateRanking;
import ch.jester.model.PlayerCard;
import ch.jester.model.Ranking;
import ch.jester.model.RankingEntry;
import ch.jester.model.RankingSystemPoint;
import ch.jester.model.Round;
import ch.jester.model.Tournament;
import ch.jester.model.factories.ModelFactory;
import ch.jester.system.api.ranking.IRankingSystem;
import ch.jester.system.exceptions.NotAllResultsException;
import ch.jester.system.ranking.impl.RankingHelper;

/**
 * Feinwertung nach Buchholz
 *
 */
public class BuchholzRankingSystem implements IRankingSystem, IEPEntryConfig {
	private ServiceUtility mServiceUtil = new ServiceUtility();
	private IEPEntry<?> rankingSystemEntry;

	@Override
	public Map<Category, Ranking> calculateRanking(Tournament tournament, IProgressMonitor pMonitor) throws NotAllResultsException {
		Map<Category, Ranking> rankingMap = new LinkedHashMap<Category, Ranking>();
		List<Category> categories = tournament.getCategories();
		for (Category category : categories) {
			rankingMap.put(category, calculateRanking(category, pMonitor));
		}
		return rankingMap;
	}

	@Override
	public Ranking calculateRanking(Category category, IProgressMonitor pMonitor) throws NotAllResultsException {
		Ranking ranking = null;
		if (checkCategoryFinished(category)) {
			return createFinalRanking(category);
		} else {
			return createIntermediateRanking(category);
		}
	}
	
	private int getLastFinishedRoundNumber(Category category){
		Round r = RankingHelper.getLastFinishedRound(category);
		if(r==null){
			return 0;
		}
		return r.getNumber();
	}
	
	@Override
	public Ranking calculateRanking(Category category, Round round,
			IProgressMonitor pMonitor) throws NotAllResultsException {
		return createIntermediateRanking(category, round, getLastFinishedRoundNumber(category));
	}
	
	/**
	 * Erzeugt die Schlussrangliste zu einer Kategorie
	 * @param category
	 * @return Schlussrangliste
	 * @throws NotAllResultsException 
	 */
	private FinalRanking createFinalRanking(Category category) throws NotAllResultsException {
		if (!RankingHelper.allResultsAvailable(category)) throw new NotAllResultsException("NotAllResultsForRanking");
		FinalRanking ranking = category.getRanking();
		if (ranking != null) {
			ranking.getRankingEntries().clear();
			//mServiceUtil.getDaoServiceByEntity(RankingEntry.class).delete(ranking.getRankingEntries());
			//ranking.setRankingEntries(new ArrayList<RankingEntry>());
		} else {
			ranking = ModelFactory.getInstance().createFinalRanking(category);
		}
		
		List<PlayerCard> initialFinalRanking = RankingHelper.getInitialFinalRanking(category);
		
		for (PlayerCard playerCard : initialFinalRanking) {
			List<PlayerCard> opponents = RankingHelper.getOpponents(playerCard, category.getRounds());
			initRankingSystemPoint(playerCard);
			double opponentPoints = calculateBuchholzPoints(opponents);
			RankingSystemPoint rankingSystemPoint = playerCard.getRankingSystemPoint(rankingSystemEntry.getProperty("shortType"));
			rankingSystemPoint.setPoints(opponentPoints);
		}
		
		RankingHelper.createRanking(ranking, initialFinalRanking, rankingSystemEntry.getProperty("shortType"));
		
		RankingHelper.printRanking(ranking); // TODO Peter: Bei Task complete rauslöschen!
		
		saveFinalRanking(category, ranking);
		return ranking;
	}
	
	private IntermediateRanking createIntermediateRanking(Category category,Round injectedRound, int lastFinishedRoundNumber) throws NotAllResultsException {
		if (lastFinishedRoundNumber == 0) throw new NotAllResultsException("NotAllResultsForRanking");
		if(lastFinishedRoundNumber<injectedRound.getNumber()) throw new NotAllResultsException("NotAllResultsForRanking");
		IntermediateRanking ranking = injectedRound.getRanking();
		if (ranking != null) {
			//mServiceUtil.getDaoServiceByEntity(RankingEntry.class).delete(ranking.getRankingEntries());
			ranking.getRankingEntries().clear();
			//mServiceUtil.getDaoServiceByEntity(RankingEntry.class).delete(ranking.getRankingEntries());
		} else {
			ranking = ModelFactory.getInstance().createIntermediateRanking(injectedRound);
		}
		
		List<Round> finishedRounds = RankingHelper.getFinishedRounds(category);
		List<PlayerCard> initialIntermediateRanking = RankingHelper.getInitialIntermediateRanking(category);
		
		for (PlayerCard playerCard : initialIntermediateRanking) {
			List<PlayerCard> opponents = RankingHelper.getOpponents(playerCard, finishedRounds);
			initRankingSystemPoint(playerCard);
			double opponentPoints = calculateBuchholzPoints(opponents);
			RankingSystemPoint rankingSystemPoint = playerCard.getRankingSystemPoint(rankingSystemEntry.getProperty("shortType"));
			rankingSystemPoint.setPoints(opponentPoints);
		}
		
		RankingHelper.createRanking(ranking, initialIntermediateRanking, rankingSystemEntry.getProperty("shortType"));
		
		saveIntermediateRanking(injectedRound, ranking);
		return ranking;
	}
	
	/**
	 * Erzeugt eine Zwischenrangliste zur letzten fertig gespielten Runde
	 * @param category
	 * @return Zwischenrangliste
	 * @throws NotAllResultsException 
	 */
	private IntermediateRanking createIntermediateRanking(Category category) throws NotAllResultsException {
		Round lastFinishedRound = RankingHelper.getLastFinishedRound(category); 
		return createIntermediateRanking(category,lastFinishedRound, getLastFinishedRoundNumber(category));
		
	}

	/**
	 * Berechnen der Summe der erzielten Punkte aller Gegner
	 * -> Eigentlicher Buchholz Algorithmus
	 * @param opponents
	 * @return Summe der Punkte der Gegner
	 */
	private double calculateBuchholzPoints(List<PlayerCard> opponents) {
		double opponentPoints = 0.0;
		for (PlayerCard opponent : opponents) {
			opponentPoints += opponent.getPoints();
		}
		return opponentPoints;
	}

	/**
	 * Prüfen ob die PlayerCard bereits einen Buchholz-RankingSystemPoint hat.
	 * Falls nicht wird ein neuer erzeugt.
	 * Falls es schon einen gibt, werden die Punkte auf 0.0 zurückgesetzt.
	 * @param playerCard
	 */
	private void initRankingSystemPoint(PlayerCard playerCard) {
		RankingSystemPoint rankingSystemPoint = playerCard.getRankingSystemPoint(rankingSystemEntry.getProperty("shortType"));
		if (rankingSystemPoint == null ) {
			rankingSystemPoint = ModelFactory.getInstance().createRankingSystemPoint(rankingSystemEntry.getProperty("shortType"));
			playerCard.addRankingSystemPoint(rankingSystemPoint);
		} else {
			rankingSystemPoint.setPoints(0.0);
		}
	}

	/**
	 * Persistieren der Schlussrangliste zu der Kategorie
	 * @param category
	 * @param ranking
	 */
	private void saveFinalRanking(Category category, FinalRanking ranking) {
		category.setRanking(ranking);
		IDaoService<Category> categoryDaoService = mServiceUtil.getDaoServiceByEntity(Category.class);
		categoryDaoService.save(category);
	}

	/**
	 * Persistieren der Zwischenrangliste zu der letzetn Runde
	 * @param lastFinishedRound
	 * @param ranking
	 */
	private void saveIntermediateRanking(Round lastFinishedRound, IntermediateRanking ranking) {
		lastFinishedRound.setRanking(ranking);
		IDaoService<Round> roundDaoService = mServiceUtil.getDaoServiceByEntity(Round.class);
		roundDaoService.save(lastFinishedRound);
	}
	
	/**
	 * Überprüfen ob bei allen Runden alle Resultate erfasst wurden.
	 * So ist das Turnier in dieser Kategorie zu Ende und es kann ein FinalRanking gemacht werden.
	 * @param category
	 * @return true wenn alle Resultate zu allen Runden erfasst wurden.
	 */
	private boolean checkCategoryFinished(Category category) {
		List<Round> finishedRounds = RankingHelper.getFinishedRounds(category);
		return finishedRounds.size() == category.getRounds().size();
	}

	@Override
	public void setEPEntry(IEPEntry<?> e) {
		rankingSystemEntry = e;
	}


}
