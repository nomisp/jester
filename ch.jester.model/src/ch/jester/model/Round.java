package ch.jester.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="Round")
public class Round implements Serializable {
	private static final long serialVersionUID = 6672346214824111918L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;
	
	@Column(name="RoundNumber", nullable=false)
	private int number;
	
	@ManyToOne
	private Category category;
	
	@OneToMany(mappedBy="round")
	private Set<Pairing> pairings = new HashSet<Pairing>();

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public Set<Pairing> getPairings() {
		return pairings;
	}

	public void setPairings(Set<Pairing> pairings) {
		this.pairings = pairings;
	}
	
	public void addPairing(Pairing pairing) {
		if (pairing == null) throw new IllegalArgumentException("pairing may not be null");
		this.pairings.add(pairing);
		if (pairing.getRound() != this) pairing.setRound(this);
	}

	public void removePairing(Pairing pairing) {
		if (pairing == null) throw new IllegalArgumentException("pairing may not be null");
		this.pairings.remove(pairing);
	}
}
