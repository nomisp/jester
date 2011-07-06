package ch.jester.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

/**
 * Wrapper-Klasse für die Punkte der verschiedenen Feinwertungen.
 * @author Peter
 *
 */
@Entity
//@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
//@DiscriminatorColumn(name = "RankingSystem", discriminatorType = DiscriminatorType.STRING, length = 50)
//@Table(name = "RankingSystemPoint", uniqueConstraints = {@UniqueConstraint(columnNames = {"RankingSystem"})})
public class RankingSystemPoint extends AbstractModelBean<RankingSystemPoint> implements Comparable<RankingSystemPoint> {
	private static final long serialVersionUID = -3533894872834400L;
   
    @Column(name = "RankingSystem", nullable = false, length = 50)
    private String rankingSystem;

	@Column(name = "Points")
    private Double points = 0.0;
	
	@ManyToOne
	private PlayerCard playerCard;

	public RankingSystemPoint() {
		
	}
	
	public RankingSystemPoint(String rankingSystem) {
		this.rankingSystem = rankingSystem;
	}
	
	public String getRankingSystem() {
		return rankingSystem;
	}

	public void setRankingSystem(String rankingSystem) {
		this.rankingSystem = rankingSystem;
	}

	public Double getPoints() {
		return points;
	}

	public void setPoints(Double points) {
		this.points = points;
	}
	
	/**
	 * Addieren von Feinwertungspunkten
	 * @param points
	 */
	public void addPoints(Double points) {
		this.points += points;
	}

	public PlayerCard getPlayerCard() {
		return playerCard;
	}

	public void setPlayerCard(PlayerCard playerCard) {
		this.playerCard = playerCard;
		playerCard.addRankingSystemPoint(this);	// Bidirektion
	}
	
	@Override
	public Object clone() throws CloneNotSupportedException {
		return createCompleteClone();
	}

	@Override
	public int compareTo(RankingSystemPoint rankingSystemPoint) {
		return this.points.compareTo(rankingSystemPoint.getPoints());
	}
}
