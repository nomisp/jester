package ch.jester.model;

import java.util.ArrayList;
import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlIDREF;
import javax.xml.bind.annotation.XmlTransient;

@Entity
@Table(name="Round")
@NamedQueries({
	@NamedQuery(name="AllRounds", query="select r from Round r order by r.number"),
	@NamedQuery(name="RoundByNumber", query="select r from Round r where r.number = :number"),
	@NamedQuery(name="OpenRoundsByCategory", query="select distinct r from Round r join fetch r.pairings p where r.category = :category and p.result is null and p.black.player is not null and p.white.player is not null order by r.number"),
	@NamedQuery(name="FinishedRoundsByCategory", query="select distinct r from Round r join fetch r.pairings p where r.category = :category and p.result is not null order by r.number"),
	@NamedQuery(name="FinishedRoundsWithoutRankingByCategory", query="select distinct r from Round r join fetch r.pairings p where r.category = :category and p.result is not null and r.ranking is null order by r.number")
})
public class Round extends AbstractModelBean<Round> implements Comparable<Round> {
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

	@OneToOne(cascade=CascadeType.ALL, orphanRemoval=true)
	private IntermediateRanking ranking;
	
	public Integer getNumber() {
		return number;
	}

	public void setNumber(Integer number) {
		changeProperty("number", number);
	}

	@XmlIDREF
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
		if (!this.pairings.contains(pairing)) this.pairings.add(pairing);
		if (pairing.getRound() != this) pairing.setRound(this);
	}

	public void removePairing(Pairing pairing) {
		if (pairing == null) throw new IllegalArgumentException("pairing may not be null");
		this.pairings.remove(pairing);
		/*if(removed){
			category.re
		}*/
	}
	

	public void removeAllPairings(List<Pairing> pairings) {
		if (pairings == null) throw new IllegalArgumentException("pairing may not be null");
		this.pairings.removeAll(pairings);
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		changeProperty("date", date);
	}

	@XmlTransient
	public IntermediateRanking getRanking() {
		return ranking;
	}

	public void setRanking(IntermediateRanking ranking) {
		this.ranking = ranking;
	}
	

	@Override
	public Object clone() throws CloneNotSupportedException {
		return createCompleteClone();
	}

	@Override
	public int compareTo(Round arg0) {
		if(arg0.number == number){return 0;};
		return number<arg0.number?-1:1;
	}
}
