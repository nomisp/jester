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

/**
 * Entität für die Tabelle Tournament 
 * @author Peter
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
	@org.hibernate.validator.constraints.Length(min=2, max=50)
	private String name;
	
	@OneToMany
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
		categories.add(cat);
	}
	
	public void removeCategory(Category cat) {
		categories.remove(cat);
	}
}
