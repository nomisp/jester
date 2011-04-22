package ch.jester.system.ranking.impl;

import ch.jester.common.components.EPEntry;
import ch.jester.system.api.ranking.IRankingSystem;
import ch.jester.system.api.ranking.IRankingSystemEntry;

public class DefaultRankingSystemEntry extends EPEntry<IRankingSystem> implements IRankingSystemEntry {

	public DefaultRankingSystemEntry(IRankingSystem pService) {
		super(pService);
	}

	@Override
	public String getShortType() {
		return getProperty(IRankingSystemEntry.SHORTTYPE);
	}

	@Override
	public String getDescription() {
		return getProperty(IRankingSystemEntry.TYPEDESCRIPTION);
	}
	
	public String toString() {
		return !getDescription().isEmpty() ? getDescription() : getShortType();
	}

}
