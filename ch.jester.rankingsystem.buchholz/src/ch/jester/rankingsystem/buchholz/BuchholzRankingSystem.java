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
import ch.jester.model.Player;
import ch.jester.model.PlayerCard;
import ch.jester.model.Tournament;
import ch.jester.orm.ORMPlugin;
import ch.jester.system.api.ranking.IRankingSystem;
import ch.jester.system.exceptions.NotAllResultsException;

/**
 * Feinwertung nach Buchholz
 * @author Peter
 *
 */
public class BuchholzRankingSystem implements IRankingSystem {
	private ServiceUtility mServiceUtil = new ServiceUtility();

	@Override
	public Map<Category, List<PlayerCard>> calculateRanking(Tournament tournament, IProgressMonitor pMonitor) throws NotAllResultsException {
		Map<Category, List<PlayerCard>> rankingMap = new HashMap<Category, List<PlayerCard>>();
		List<Category> categories = tournament.getCategories();
		for (Category category : categories) {
			rankingMap.put(category, calculateRanking(category, pMonitor));
		}
		return rankingMap;
	}

	@Override
	public List<PlayerCard> calculateRanking(Category category, IProgressMonitor pMonitor) throws NotAllResultsException {
		List<PlayerCard> ranking = initialRanking(category);
		
		return ranking;
	}
	
	private List<PlayerCard> initialRanking(Category category) {
//		IDaoService<Category> categoryDao = mServiceUtil.getDaoServiceByEntity(Category.class);
		EntityManager entityManager = ORMPlugin.getJPAEntityManager();
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
