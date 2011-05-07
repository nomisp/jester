package ch.jester.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import ch.jester.commonservices.api.persistency.IDaoObject;

@Entity
@Table(name="Category")
public class Category implements Serializable, IDaoObject {
	private static final long serialVersionUID = 6845187372965814476L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;

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
	
	@OneToMany(mappedBy="category")
//	@JoinTable(name = "CategoryRoundAss",
//	        joinColumns = {@JoinColumn(name = "CategoryId")},
//	        inverseJoinColumns = {@JoinColumn(name = "RoundId")})
	private Set<Round> rounds = new HashSet<Round>();

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

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

	public Set<Round> getRounds() {
		return rounds;
	}

	public void setRounds(Set<Round> rounds) {
		this.rounds = rounds;
	}
	
	public void addRound(Round round) {
		if (round == null) throw new IllegalArgumentException("round may not be null");
		this.rounds.add(round);
		if (round.getCategory() != this) round.setCategory(this);
	}
	
	public void removeRound(Round round) {
		if (round == null) throw new IllegalArgumentException("round may not be null");
		this.rounds.remove(round);
	}
}
