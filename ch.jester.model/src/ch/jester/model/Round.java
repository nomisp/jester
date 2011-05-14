package ch.jester.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="Round")
@NamedQueries({
	@NamedQuery(name="AllRounds", query="select r from Round r order by r.number"),
	@NamedQuery(name="RoundByNumber", query="select r from Round r where r.number = :number")
})
public class Round extends AbstractModelBean{
	private static final long serialVersionUID = 6672346214824111918L;

	
	@Column(name="RoundNumber", nullable=false)
	private Integer number;
	
	@ManyToOne
	private Category category;
	
	@OneToMany(mappedBy="round")
	private Set<Pairing> pairings = new HashSet<Pairing>();

	public Integer getNumber() {
		return number;
	}

	public void setNumber(Integer number) {
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

	@Override
	public Object clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return null;
	}
}
