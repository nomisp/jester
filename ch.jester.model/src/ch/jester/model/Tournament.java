package ch.jester.model;

import java.io.Serializable;
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

import ch.jester.dao.IDAO;

/**
 * Entit�t f�r die Tabelle Tournament 
 *
 */
@Entity
@Table(name="Tournament")
@NamedQueries({
	@NamedQuery(name="AllTournaments", query="select t from Tournament t order by t.name, t.dateFrom"),
	@NamedQuery(name="TournamentByName", query="select t from Tournament t where t.name like :name")
})
public class Tournament implements Serializable, IDAO {
	private static final long serialVersionUID = -3356578830307874396L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;
	
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
	
	@Column(name="RankingSystem", nullable=false)
	private String rankingSystem; // Feinwertung (als deklarativer Service implementiert) entspricht dem EP-Attribut: shortType
	
	@Column(name="EloCalculator", nullable=false)
	private String eloCalculator; // EloCalculator (als deklarativer Service implementiert) entspricht dem EP-Attribut: shortType
	
	@OneToMany
	@JoinTable(name = "TournamentCategoryAss",
	        joinColumns = {@JoinColumn(name = "TournamentId")},
	        inverseJoinColumns = {@JoinColumn(name = "CategoryId")})
	private Set<Category> categories = new HashSet<Category>();

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

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
}
