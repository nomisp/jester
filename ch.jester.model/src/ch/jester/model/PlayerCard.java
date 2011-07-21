package ch.jester.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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

import ch.jester.model.util.ColorPreference;

/**
 * Spielerkarte um einem Spieler für das Turnier relevante Daten
 * wie zum Beispiel seine Startnummer anzuhängen.
 * 
 *
 */
@Entity
@Table(name="PlayerCard")
@NamedQueries({
	@NamedQuery(name="PlayerCardsByCategoryOrderByPoints", query="select pc from PlayerCard pc where pc.category = :category and pc.player is not null order by pc.points desc"),
	@NamedQuery(name="PlayerCardsByCategoryAndFinishedRoundsOrderByPoints", query="select pc from PlayerCard pc where :category = pc.category and exists (select r from Round r where r member of pc.category.rounds and r in (:finishedRounds)) and pc.player is not null order by pc.points desc")
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
	
	@Column(nullable=true)
	private Integer byes = 0;	// Anzahl Freilose
	
	@Column(nullable=true)
	@Enumerated(EnumType.STRING)
	private Float floating;		// Wenn ein Spieler im Schweizer-System in eine andere Punktegruppe gepaart wurde
	
	@Column
	private Boolean active = Boolean.TRUE;	// Beschreibt ob ein Spieler noch aktiv im Turnier ist
	
	@Column(nullable=true)
	private String colors;
	
	@Column(nullable=true)
	private String roundPoints;
	
	private transient ColorPreference colorPref;
	
	@XmlAttribute(name="playerId")
	@XmlIDREF
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
	void addResult(Double result) {
		if (result != null) {
			this.points += result;
			addRoundPoint(result);
			/*if (result == 0.5) {
				addRoundPoint("x");
			} else {
				addRoundPoint(String.valueOf(result));
			}*/
		}
	}
	
	/**
	 * Ändern eines Resultates
	 * @param oldResult	altes Resultat
	 * @param newResult	neues Resultat
	 * @param roundNr	Nummer der Runde der Resultatänderung
	 */
	public void modifyResult(Double oldResult, Double newResult, int roundNr) {
		if (oldResult != null && newResult != null && roundNr > 0 && roundNr < roundPoints.length()) {
			this.points -= oldResult;
			this.points += newResult;
			
			char[] charArray = roundPoints.toCharArray();
			if (newResult == 0.5) {
				charArray[roundNr-1] = 'x';
			} else {
				charArray[roundNr-1] = (char) newResult.intValue();
			}
			roundPoints = charArray.toString();
		}
	}

	public List<RankingSystemPoint> getRankingSystemPoints() {
		return rankingSystemPoints;
	}
	
	/**
	 * Liefert den RankingSystemPoint zu einem bestimmten Rankingsystem
	 * @param rankingSystem
	 * @return RankingSystemPoint oder null falls es keines zu diesem Ranking-System gibt
	 */
	
	public RankingSystemPoint getRankingSystemPoint(String rankingSystem) {
		for (RankingSystemPoint rankingSystemPoint : rankingSystemPoints) {
			if (rankingSystemPoint.getRankingSystem().equals(rankingSystem)) return rankingSystemPoint;
		}
		return null;
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

	public Integer getByes() {
		return byes;
	}

	public void setByes(Integer byes) {
		this.byes = byes;
	}
	
	/**
	 * Der Spieler hat ein Freilos bekommen. Die Anzahl Freilose wird um 1 erhöht.
	 */
	public void addBye() {
		this.byes++;
	}

	public Float getFloating() {
		return floating;
	}

	public void setFloating(Float floating) {
		this.floating = floating;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	/**
	 * Gibt die gespielten Farben als String
	 * @return Farben als zusammengehängter String z.B. "wbwbwwb"
	 */
	public String getColors() {
		return colors;
	}

	/**
	 * Setzten eines Farbstrings, welcher die gespielten Farben repräsentiert.<br/>
	 * Sollte in der Regel über<br/>
	 * <code>addWhite()</code> oder <code>addBlack()</code> gemacht werden.
	 * @param colors Farbstring mit w für Weiss und b für Schwarz (black) z.B. "wbwbwwb"
	 */
	public void setColors(String colors) {
		this.colors = colors;
	}
	
	/**
	 * Hinzufügen der Weissen-Farbe, wenn ein Spieler mit weiss gespielt hat
	 */
	public void addWhite() {
		if (colors == null) {
			colors = "w";
		} else {
			colors += "w";
		}
	}
	
	/**
	 * Hinzufügen der Schwarzen-Farbe, wenn ein Spieler mit schwarz gespielt hat
	 */
	public void addBlack() {
		if (colors == null) {
			colors = "b";
		} else {
			colors += "b";
		}
	}

	public String getRoundPoints() {
		return roundPoints;
	}

	public void setRoundPoints(String roundPoints) {
		this.roundPoints = roundPoints;
	}
	
	private void addRoundPoint(Double d){
		String result;
		if(d==0.5d){
			result = "x";
		}else if(d==0.0d){
			result = "0";
		}else if(d==1.0d){
			result = "1";
		}else{
			throw new IllegalArgumentException("points must be 1.0, 0.0 or 0.5");
		}
		addRoundPoint(result);
	}
	
	/**
	 * Hinzufügen von Punkten einer Runde
	 * @param points 1=Sieg, x=Remis, 0=Verloren 
	 */
	private void addRoundPoint(String points) {
		if (!(points.length() == 1 &&(
				 points.equals("1") 
				|| points.equals("0") 
				|| points.equalsIgnoreCase("x")))) throw new IllegalArgumentException("points must be 1, 0 or x");
		if (roundPoints == null) {
			roundPoints = points;
		} else {
			roundPoints += points;
		}
	}
	
	/**
	 * Liefert die Summe der Punkte des Spielers bis zu einer bestimmten Runde 
	 * @param roundNumber	Runde bis zu welcher die Punkte gebraucht werden
	 * @return
	 */
	public Double getPointsTillRound(int roundNumber) {
		double points = 0.0;
		char[] pointsArray = roundPoints.toCharArray();
		for (int i = 0; i < roundNumber; i++) {
			char point = pointsArray[i];
			if (point == 'x' || point == 'X') {
				point += 0.5;
			} else if (point == '1') {
				point += 1.0;
			}
		}
		return points;
	}

	public ColorPreference getColorPref() {
		return colorPref;
	}

	public void setColorPref(ColorPreference colorPref) {
		this.colorPref = colorPref;
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		return createCompleteClone();
	}

	public String toString(){
		if(player!=null){
			return "PlayerCard: "+player.getLastName()+", "+player.getFirstName()+": "+getPoints();
		}else{
			return "NullPlayerCard";
		}
	}
}
