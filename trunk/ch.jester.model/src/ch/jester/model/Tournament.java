package ch.jester.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 * Entität für die Tabelle Tournament 
 *
 */
@Entity
@Table(name="Tournament")
@NamedQueries({
	@NamedQuery(name="AllTournaments", query="select t from Tournament t order by t.name, t.dateFrom"),
	@NamedQuery(name="countTournaments",query="SELECT count(Tournament) FROM Tournament"),
	@NamedQuery(name="TournamentByName", query="select t from Tournament t where t.name like :name")
})
public class Tournament extends AbstractModelBean {
	private static final long serialVersionUID = -3356578830307874396L;

	@Column(name="Name", nullable=false, length=50)
	//@org.hibernate.validator.constraints.Length(min=2, max=50)
	@NotNull
	private String name;
	
	@Column(name="Description", nullable=true, length=255)
	private String description;
	
	@Column(name="Year", nullable=true)
	private int year;
	
	@Column(name="DateFrom", nullable=true)
	private Date dateFrom;
	
	@Column(name="DateTo", nullable=true)
	private Date dateTo;
	
	@Column(name="PairingSystem", nullable=false)
	private String pairingSystem; // Paarungssystem (als deklarativer Service implementiert) entspricht dem EP-Attribut: class
	
	@Column(name="RankingSystem", nullable=false)
	private String rankingSystem; // Feinwertung (als deklarativer Service implementiert) entspricht dem EP-Attribut: class
	
	@Column(name="EloCalculator", nullable=false)
	private String eloCalculator; // EloCalculator (als deklarativer Service implementiert) entspricht dem EP-Attribut: class
	
	@OneToMany
	@JoinTable(name = "TournamentCategoryAss",
	        joinColumns = {@JoinColumn(name = "TournamentId")},
	        inverseJoinColumns = {@JoinColumn(name = "CategoryId")})
	private Set<Category> categories = new HashSet<Category>();

	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public Date getDateFrom() {
		return dateFrom;
	}

	public void setDateFrom(Date dateFrom) {
		this.dateFrom = dateFrom;
	}

	public Date getDateTo() {
		return dateTo;
	}

	public void setDateTo(Date dateTo) {
		this.dateTo = dateTo;
	}

	public String getPairingSystem() {
		return pairingSystem;
	}

	public void setPairingSystem(String pairingSystem) {
		this.pairingSystem = pairingSystem;
	}

	public String getRankingSystem() {
		return rankingSystem;
	}

	public void setRankingSystem(String rankingSystem) {
		this.rankingSystem = rankingSystem;
	}

	public String getEloCalculator() {
		return eloCalculator;
	}

	public void setEloCalculator(String eloCalculator) {
		this.eloCalculator = eloCalculator;
	}

	public Set<Category> getCategories() {
		return categories;
	}

	public void setCategories(Set<Category> categories) {
		this.categories = categories;
	}
	
	public void addCategory(Category cat) {
		if (cat == null) throw new IllegalArgumentException("category may not be null");
		this.categories.add(cat);
	}
	
	public void removeCategory(Category cat) {
		if (cat == null) throw new IllegalArgumentException("category may not be null");
		this.categories.remove(cat);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((categories == null) ? 0 : categories.hashCode());
		result = prime * result
				+ ((dateFrom == null) ? 0 : dateFrom.hashCode());
		result = prime * result + ((dateTo == null) ? 0 : dateTo.hashCode());
		result = prime * result
				+ ((description == null) ? 0 : description.hashCode());
		result = prime * result
				+ ((eloCalculator == null) ? 0 : eloCalculator.hashCode());
		result = prime * result + id;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result
				+ ((pairingSystem == null) ? 0 : pairingSystem.hashCode());
		result = prime * result
				+ ((rankingSystem == null) ? 0 : rankingSystem.hashCode());
		result = prime * result + year;
		return result;
	}

	/*@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Tournament other = (Tournament) obj;
		if (categories == null) {
			if (other.categories != null)
				return false;
		} else if (!categories.equals(other.categories))
			return false;
		if (dateFrom == null) {
			if (other.dateFrom != null)
				return false;
		} else if (!dateFrom.equals(other.dateFrom))
			return false;
		if (dateTo == null) {
			if (other.dateTo != null)
				return false;
		} else if (!dateTo.equals(other.dateTo))
			return false;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (eloCalculator == null) {
			if (other.eloCalculator != null)
				return false;
		} else if (!eloCalculator.equals(other.eloCalculator))
			return false;
		if (id != other.id)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (pairingSystem == null) {
			if (other.pairingSystem != null)
				return false;
		} else if (!pairingSystem.equals(other.pairingSystem))
			return false;
		if (rankingSystem == null) {
			if (other.rankingSystem != null)
				return false;
		} else if (!rankingSystem.equals(other.rankingSystem))
			return false;
		if (year != other.year)
			return false;
		return true;
	}*/
	
	@Override
	public Object clone() throws CloneNotSupportedException {
		Tournament clone = new Tournament();
		clone.id = 0;
		clone.name = this.name;
		clone.description = this.description;
		clone.year = this.year;
		clone.dateFrom = this.dateFrom;
		clone.dateTo = this.dateTo;
		clone.pairingSystem = this.pairingSystem;
		clone.rankingSystem = this.rankingSystem;
		clone.eloCalculator = this.eloCalculator;
		
		return clone;
	}
}
