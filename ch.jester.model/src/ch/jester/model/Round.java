package ch.jester.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="Round")
@NamedQueries({
	@NamedQuery(name="AllRounds", query="select r from Round r order by r.number"),
	@NamedQuery(name="RoundByNumber", query="select r from Round r where r.number = :number")
})
public class Round extends AbstractModelBean<Round> {
	private static final long serialVersionUID = 6672346214824111918L;

	
	@Column(name="RoundNumber", nullable=false)
	private Integer number;
	
	@ManyToOne
	private Category category;
	
	@OneToMany(mappedBy="round", cascade={CascadeType.ALL}, orphanRemoval=true)
	private List<Pairing> pairings = new ArrayList<Pairing>();
	
	@Column(name="Date", nullable=true)
	@Temporal(TemporalType.DATE)
	private Date date;

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

	public List<Pairing> getPairings() {
		return pairings;
	}

	public void setPairings(List<Pairing> pairings) {
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

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		return createCompleteClone();
	}
}
