package ch.jester.system.ranking.impl;

import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;

import ch.jester.common.components.AbstractEPComponent;
import ch.jester.model.Category;
import ch.jester.model.Player;
import ch.jester.system.api.ranking.IRankingSystem;
import ch.jester.system.api.ranking.IRankingSystemEntry;
import ch.jester.system.api.ranking.IRankingSystemManager;
import ch.jester.system.exceptions.NotAllResultsException;
import ch.jester.system.internal.SystemActivator;

public class DefaultRankingSystemManager extends AbstractEPComponent<IRankingSystemEntry, IRankingSystem> implements IRankingSystemManager {
	
	public DefaultRankingSystemManager() {
		super(IRankingSystem.class, 
				SystemActivator.getInstance().getActivationContext(), 
				"ch.jester.system.api", 
				"RankingSystem");		
	}

	@Override
	protected IRankingSystemEntry createEntry(IRankingSystem o) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Player> doCalculateRanking(IRankingSystemEntry rankingSystem,
			IProgressMonitor progressMonitor) throws NotAllResultsException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Player> doCalculateRanking(Category category,
			IRankingSystemEntry rankingSystem, IProgressMonitor progressMonitor)
			throws NotAllResultsException {
		// TODO Auto-generated method stub
		return null;
	}
}
