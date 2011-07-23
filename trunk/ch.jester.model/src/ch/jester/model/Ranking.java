/**
 * 
 */
package ch.jester.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * Superklasse für die verschiedenen Ranglisten (Finale -oder ZwischenRangliste)
 *
 */
@Entity
@Table(name="Ranking")
@Inheritance
@DiscriminatorColumn(name="RankingType")
public abstract class Ranking extends AbstractModelBean<Ranking> {
	private static final long serialVersionUID = -3487670510877009183L;

	@OneToMany(mappedBy="ranking", cascade={CascadeType.ALL}, orphanRemoval=true)
	private List<RankingEntry> rankingEntries = new ArrayList<RankingEntry>();

	public List<RankingEntry> getRankingEntries() {
		return rankingEntries;
	}

	public void setRankingEntries(List<RankingEntry> rankingEntries) {
		this.rankingEntries = rankingEntries;
	}
	
	public void addRankingEntry(RankingEntry rankingEntry) {
		if (rankingEntry == null) throw new IllegalArgumentException("rankingEntry cannot be null");
		if (!rankingEntries.contains(rankingEntry)) rankingEntries.add(rankingEntry);
		if (rankingEntry.getRanking() != this) rankingEntry.setRanking(this);
	}

	protected void preRemove() {
		List<RankingEntry> entries = rankingEntries;
		for(RankingEntry entry:entries){
			entry.setPlayerCard(null);
			entry.setRanking(null);
		}
		entries.clear();
		
	}
}
