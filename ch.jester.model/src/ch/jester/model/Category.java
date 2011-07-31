package ch.jester.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlIDREF;
import javax.xml.bind.annotation.XmlTransient;

@Entity
@Table(name="Category")
@NamedQueries({
	@NamedQuery(name="AllCategories", query="select c from Category c order by c.description"),
	@NamedQuery(name="Category.getAll", query="select c from Category c order by c.description"),
	@NamedQuery(name="CategoryByName", query="select c from Category c where c.description like :description"),
	@NamedQuery(name="CategoryByPlayer", query="select c from Category c join fetch c.playerCards pc where :player = pc.player")
})
public class Category extends AbstractModelBean<Category> implements Comparable<Category>{
	private static final long serialVersionUID = 6845187372965814476L;


	@Column(name="Description", nullable=false, length=20)
	//@org.hibernate.validator.constraints.Length(min=2, max=20)
	@NotNull
	private String description;
	
	@Column(name="MinimumElo", nullable=true)
	private Integer minElo;
	
	@Column(name="MaximumElo", nullable=true)
	private Integer maxElo;
	
	@Column(name="MinimumAge", nullable=true)
	private Integer minAge;
	
	@Column(name="MaximumAge", nullable=true)
	private Integer maxAge;
	
	@Column(name="MaxRounds")
	private Integer maxRounds; // Maximale Anzahl Runden, die in dieser Kategorie gespielt werden
	

	@OneToMany(mappedBy="category", cascade={CascadeType.ALL}, orphanRemoval=true)
	private List<Round> rounds = new ArrayList<Round>();
	
	@OneToMany(mappedBy="category", cascade={CascadeType.ALL}, orphanRemoval=true)
	private List<PlayerCard> playerCards = new ArrayList<PlayerCard>();
	
	@ManyToOne
	private Tournament tournament;

	@OneToOne(cascade=CascadeType.ALL, orphanRemoval=true)
	private FinalRanking ranking;

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		changeProperty("description", description);
	}

	public Integer getMinElo() {
		return minElo;
	}

	public void setMinElo(Integer minElo) {
		changeProperty("minElo", minElo);
	}

	public Integer getMaxElo() {
		return maxElo;
	}

	public void setMaxElo(Integer maxElo) {
		changeProperty("maxElo", maxElo);
	}

	public Integer getMinAge() {
		return minAge;
	}

	public void setMinAge(Integer minAge) {
		changeProperty("minAge", minAge);
	}

	public Integer getMaxAge() {
		return maxAge;
	}

	public void setMaxAge(Integer maxAge) {
		changeProperty("maxAge", maxAge);
	}

	public Integer getMaxRounds() {
		return maxRounds;
	}

	public void setMaxRounds(Integer maxRounds) {
		this.maxRounds = maxRounds;
	}

	public List<Round> getRounds() {
		return rounds;
	}

	public void setRounds(List<Round> rounds) {
		this.rounds = rounds;
	}
	
	public void addRound(Round round) {
		if (round == null) throw new IllegalArgumentException("round may not be null");
		if (!this.rounds.contains(round)) this.rounds.add(round);
		if (round.getCategory() != this) round.setCategory(this);
		Collections.sort(rounds);
	}
	
	public void removeRound(Round round) {
		if (round == null) throw new IllegalArgumentException("round may not be null");
		this.rounds.remove(round);
	}
	public void removeRounds() {
		this.rounds.clear();
		
	}

	public List<PlayerCard> getPlayerCards() {
		return playerCards;
	}
	
	@XmlTransient
	public boolean isPaired(){
		if(this.getRounds()!=null && !this.getRounds().isEmpty()){
			return true;
		}
		return false;
	}

	public void setPlayerCards(List<PlayerCard> playerCards) {
		this.playerCards = playerCards;
	}
	
	public void addPlayerCard(PlayerCard player) {
		if (player == null) throw new IllegalArgumentException("player may not be null");
		if (!this.playerCards.contains(player)) this.playerCards.add(player);
	}
	
	public void removePlayerCard(PlayerCard player) {
		//TODO: CHECK. NOTWENDIG --> Szenario: Spieler entfernen wenn noch nicht ausgelost
		if (player == null) throw new IllegalArgumentException("player may not be null");
		this.playerCards.remove(player);
	}

	/**
	 * Liefert eine Liste der Spieler
	 * @return
	 */
	@XmlTransient
	public List<Player> getPlayers() {
		List<Player> players = new ArrayList<Player>();
		for (PlayerCard playerCard : playerCards) {
			players.add(playerCard.getPlayer());
		}
		return players;
	}

	@XmlAttribute(name="tournamentRef")
	@XmlIDREF
	public Tournament getTournament() {
		return tournament;
	}

	public void setTournament(Tournament tournament) {
		this.tournament = tournament;
	}

	@XmlTransient
	public FinalRanking getRanking() {
		return ranking;
	}

	public void setRanking(FinalRanking ranking) {
		this.ranking = ranking;
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		Category cat = cloneWithSimpleProperties();
		return cat;
	}
	
	
	public void afterUnmarshal(Unmarshaller u, Object parent) {
		    this.tournament = (Tournament)parent;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> Collection<T> getChildrenCollection(Class<T> clz) {
		if(clz==Player.class){
			return (Collection<T>) getPlayers();
		}
		if(clz==PlayerCard.class){
			return (Collection<T>) getPlayerCards();
		}
		if(clz==Round.class){
			return (Collection<T>) getRounds();
		}
		return super.getChildrenCollection(clz);
	}
	
	@Override
	public <T> boolean canGetChildrenCollection(Class<T> clz) {
		if(clz==Player.class || clz==PlayerCard.class || clz==Round.class){
			return true;
		}
		return super.canGetChildrenCollection(clz);
	}

	@Override
	public int compareTo(Category o) {
		return this.getDescription().compareTo(o.getDescription());
	}
	
}
