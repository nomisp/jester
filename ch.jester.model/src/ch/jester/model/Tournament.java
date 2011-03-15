package ch.jester.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 * Entit�t f�r die Tabelle Tournament 
 *
 */
@Entity
@Table(name="Tournament")
public class Tournament implements Serializable {
	private static final long serialVersionUID = -3356578830307874396L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;
	
	@Column(name="Name", nullable=false, length=50)
	//@org.hibernate.validator.constraints.Length(min=2, max=50)
	@NotNull
	private String name;
	
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
