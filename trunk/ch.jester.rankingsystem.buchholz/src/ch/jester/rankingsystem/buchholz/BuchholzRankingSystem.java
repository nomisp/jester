package ch.jester.rankingsystem.buchholz;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.eclipse.core.runtime.IProgressMonitor;

import ch.jester.commonservices.api.persistency.IDaoService;
import ch.jester.commonservices.util.ServiceUtility;
import ch.jester.model.Category;
import ch.jester.model.FinalRanking;
import ch.jester.model.IntermediateRanking;
import ch.jester.model.Player;
import ch.jester.model.PlayerCard;
import ch.jester.model.Ranking;
import ch.jester.model.Round;
import ch.jester.model.Tournament;
import ch.jester.model.factories.ModelFactory;
import ch.jester.orm.ORMPlugin;
import ch.jester.system.api.ranking.IRankingSystem;
import ch.jester.system.exceptions.NotAllResultsException;
import ch.jester.system.pairing.impl.PairingHelper;
import ch.jester.system.ranking.impl.RankingHelper;

/**
 * Feinwertung nach Buchholz
 * @author Peter
 *
 */
public class BuchholzRankingSystem implements IRankingSystem {
	private ServiceUtility mServiceUtil = new ServiceUtility();
	private EntityManager entityManager = ORMPlugin.getJPAEntityManager();

	@Override
	public Map<Category, Ranking> calculateRanking(Tournament tournament, IProgressMonitor pMonitor) throws NotAllResultsException {
		Map<Category, Ranking> rankingMap = new HashMap<Category, Ranking>();
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
			createIntermediateRanking(category);
		}
		
		return ranking;
	}
	
	/**
	 * Erzeugt die Schlussrangliste zu einer Kategorie
	 * @param category
	 * @return Schlussrangliste
	 * @throws NotAllResultsException 
	 */
	private FinalRanking createFinalRanking(Category category) throws NotAllResultsException {
		if (!RankingHelper.allResultsAvailable(category)) throw new NotAllResultsException("NotAllResultsForRanking");
		FinalRanking ranking = ModelFactory.getInstance().createFinalRanking(category);
		
		saveFinalRanking(category, ranking);
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
		if (lastFinishedRound == null) throw new NotAllResultsException("NotAllResultsForRanking");
		IntermediateRanking ranking = ModelFactory.getInstance().createIntermediateRanking(lastFinishedRound);
		
		saveIntermediateRanking(lastFinishedRound, ranking);
		return ranking;
	}

	/**
	 * Persistieren der Schlussrangliste zu der Kategorie
	 * @param category
	 * @param ranking
	 */
	private void saveFinalRanking(Category category, FinalRanking ranking) {
		category.setRanking(ranking);
		IDaoService<Category> roundDaoService = mServiceUtil.getDaoServiceByEntity(Category.class);
		roundDaoService.save(category);
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
		entityManager.joinTransaction();
		List<Round> finishedRounds = entityManager.createNamedQuery("FinishedRounds").getResultList();
		return finishedRounds.size() == category.getRounds().size();
	}

	private List<PlayerCard> initialRanking(Category category) {
//		IDaoService<Category> categoryDao = mServiceUtil.getDaoServiceByEntity(Category.class);
		entityManager.joinTransaction();
		List<PlayerCard> ranking = entityManager.createNamedQuery("PlayerCardsByCategoryOrderByPoints")
											.setParameter("category", category)
											.getResultList();
		for (PlayerCard playerCard : ranking) {
			System.out.println(playerCard.getPlayer() + " : " + playerCard.getPoints());
		}
		return ranking;
	}

}
