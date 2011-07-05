package ch.jester.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlIDREF;

/**
 * Spielerkarte um einem Spieler für das Turnier relevante Daten
 * wie zum Beispiel seine Startnummer anzuhängen.
 * 
 * @author Peter
 *
 */
@Entity
@Table(name="PlayerCard")
@NamedQueries({
	@NamedQuery(name="PlayerCardsByCategoryOrderByPoints", query="select pc from PlayerCard pc where :category = pc.category order by pc.points")
})
public class PlayerCard extends AbstractModelBean<PlayerCard> {
	private static final long serialVersionUID = -2710264494286525315L;
	
	@OneToOne(cascade={CascadeType.PERSIST})
	@JoinTable(name="PlayerCardPlayerAss",
				joinColumns = {@JoinColumn(name = "PlayerCardId")},
				inverseJoinColumns = {@JoinColumn(name = "PlayerId")})
	private Player player;

	@ManyToOne
	private Category category;
	
	@Column(nullable=true)
	private Integer number;
	
	@Column(nullable=true)
	private Double points = 0.0;
	
	@OneToMany(mappedBy="playerCard", cascade={CascadeType.ALL}, orphanRemoval=true)
	private List<RankingSystemPoint> rankingSystemPoints = new ArrayList<RankingSystemPoint>();
	
	@OneToOne(mappedBy="playerCard", optional=true)
	private RankingEntry rankingEntry;
	
	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		changeProperty("player", player);
	}

	public Category getCategory() {
		return category;
	}

	@XmlIDREF
	@XmlAttribute(name="categoryRef")
	public void setCategory(Category category) {
		changeProperty("category", category);
	}

	public Integer getNumber() {
		return number;
	}

	public void setNumber(Integer number) {
		changeProperty("number", number);
	}

	public Double getPoints() {
		return points;
	}

	public void setPoints(Double points) {
		this.points = points;
	}
	
	/**
	 * Hinzuaddieren der erzielten Punkte
	 * @param result Sieg: 1; Niederlage: 0; Remis: 0.5
	 */
	public void addResult(Double result) {
		if (result != null) {
			this.points += result;
		}
	}

	public List<RankingSystemPoint> getRankingSystemPoints() {
		return rankingSystemPoints;
	}

	public void setRankingSystemPoints(List<RankingSystemPoint> rankingSystemPoints) {
		this.rankingSystemPoints = rankingSystemPoints;
	}
	
	public void addRankingSystemPoint(RankingSystemPoint rankingSystemPoint) {
		if (rankingSystemPoint == null) throw new IllegalArgumentException("rankingSystemPoint may not be null");
		if (!this.rankingSystemPoints.contains(rankingSystemPoint)) this.rankingSystemPoints.add(rankingSystemPoint);
		if (rankingSystemPoint.getPlayerCard() != this) rankingSystemPoint.setPlayerCard(this); // Bidirektion
	}

	public RankingEntry getRankingEntry() {
		return rankingEntry;
	}

	public void setRankingEntry(RankingEntry rankingEntry) {
		this.rankingEntry = rankingEntry;
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		return createCompleteClone();
	}

}
