package ch.jester.system.ranking.impl;

import java.util.Map;

import org.eclipse.core.runtime.IProgressMonitor;

import ch.jester.common.components.AbstractEPComponent;
import ch.jester.model.Category;
import ch.jester.model.Ranking;
import ch.jester.model.Tournament;
import ch.jester.system.api.ranking.IRankingSystem;
import ch.jester.system.api.ranking.IRankingSystemEntry;
import ch.jester.system.api.ranking.IRankingSystemManager;
import ch.jester.system.exceptions.NotAllResultsException;
import ch.jester.system.internal.SystemActivator;

/**
 * Standardimplementation eines RankingSystemManager
 * 
 */
public class DefaultRankingSystemManager extends AbstractEPComponent<IRankingSystemEntry, IRankingSystem> implements IRankingSystemManager {
	
	public DefaultRankingSystemManager() {
		super(IRankingSystem.class, 
				SystemActivator.getInstance().getActivationContext(), 
				"ch.jester.system.api", 
				"RankingSystem");		
	}

	@Override
	protected IRankingSystemEntry createEntry(IRankingSystem o) {
		return new DefaultRankingSystemEntry(o);
	}

	@Override
	public Map<Category, Ranking> doCalculateRanking(Tournament tournament, IRankingSystemEntry rankingSystem,
			IProgressMonitor progressMonitor) throws NotAllResultsException {
		return rankingSystem.getService().calculateRanking(tournament, progressMonitor);
	}

	@Override
	public Ranking doCalculateRanking(Category category,
			IRankingSystemEntry rankingSystem, IProgressMonitor progressMonitor)
			throws NotAllResultsException {
		return rankingSystem.getService().calculateRanking(category, progressMonitor);
	}
}
