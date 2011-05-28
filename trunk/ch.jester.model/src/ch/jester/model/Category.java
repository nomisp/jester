package ch.jester.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name="Category")
@NamedQueries({
	@NamedQuery(name="AllCategories", query="select c from Category c order by c.description"),
	@NamedQuery(name="CategoryByName", query="select c from Category c where c.description like :description"),
	@NamedQuery(name="CategoryByPlayer", query="select c from Category c where :player in c.players")
})
public class Category extends AbstractModelBean<Category> {
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
//	@JoinTable(name = "CategoryRoundAss",
//	        joinColumns = {@JoinColumn(name = "CategoryId")},
//	        inverseJoinColumns = {@JoinColumn(name = "RoundId")})
	private List<Round> rounds = new ArrayList<Round>();
	
	@OneToMany
	@JoinTable(name = "CategoryPlayerAss", 
			joinColumns = {@JoinColumn(name = "CategoryId")},
	        inverseJoinColumns = {@JoinColumn(name = "PlayerId")})
	private Set<Player> players = new HashSet<Player>();
	
	@ManyToOne
	private Tournament tournament;


	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getMinElo() {
		return minElo;
	}

	public void setMinElo(Integer minElo) {
		this.minElo = minElo;
	}

	public Integer getMaxElo() {
		return maxElo;
	}

	public void setMaxElo(Integer maxElo) {
		this.maxElo = maxElo;
	}

	public Integer getMinAge() {
		return minAge;
	}

	public void setMinAge(Integer minAge) {
		this.minAge = minAge;
	}

	public Integer getMaxAge() {
		return maxAge;
	}

	public void setMaxAge(Integer maxAge) {
		this.maxAge = maxAge;
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
	}
	
	public void removeRound(Round round) {
		if (round == null) throw new IllegalArgumentException("round may not be null");
		this.rounds.remove(round);
	}

	public Set<Player> getPlayers() {
		return players;
	}

	public void setPlayers(Set<Player> players) {
		this.players = players;
	}
	
	public void addPlayer(Player player) {
		if (player == null) throw new IllegalArgumentException("round may not be null");
		this.players.add(player);
	}
	
	public void removePlayer(Player player) {
		if (player == null) throw new IllegalArgumentException("round may not be null");
		this.players.remove(player);
	}

	public Tournament getTournament() {
		return tournament;
	}

	public void setTournament(Tournament tournament) {
		this.tournament = tournament;
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return null;
	}
}
