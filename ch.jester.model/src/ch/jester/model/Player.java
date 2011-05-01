package ch.jester.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name="Player")
@NamedQueries({
@NamedQuery(name="getAll",query="SELECT player FROM Player player order by lastName, firstName"),
@NamedQuery(name="findByName",query="SELECT player FROM Player player WHERE UPPER(player.lastName) LIKE :lastName order by lastName, firstName")
})
public class Player extends AbstractModelBean{
	private static final long serialVersionUID = -2351315088207630377L;

	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;
	
	@Column(name="FirstName", nullable=false, length=50)
	//@org.hibernate.validator.constraints.Length(min=2, max=50)
	@NotNull
	private String firstName;
	
	@Column(name="LastName", nullable=false, length=50)
//	@org.hibernate.validator.constraints.Length(min=2, max=50)
	@NotNull
	private String lastName;
	
	@Column(name="City", nullable=true, length=50)
	private String city;
	
	@Column(name="Nation", nullable=true, length=50)
	private String nation;
	
	@Column(name="FideCode", nullable=true)
	private int fideCode;
	
	@Column(name="NationalCode", nullable=true)
	private int nationalCode;
	
	@Column(name="Elo", nullable=true)
	private int elo;
	
	@Column(name="NationalElo", nullable=true)
	private int nationalElo;
	
	@Column(name="Age", nullable=true)
	private int age;
	
	@Column(name="Category", nullable=true)
	private String category;	// Kategorie eines Spielers z.B. Senior, Junior, Schüler etc.
	
	@Column(name="NationalCoefficient", nullable=true)
	private int nationalCoefficient;	// Koeffizient zur Berechnung der Nationalen Elo
	
	@Column(name="FideCoefficient", nullable=true)
	private int fideCoefficient;	// Koeffizient zur Berechnung der FIDE-Elo
	
	@Column(name="Title", nullable=true)
	private String title; // Titel eines Spielers (GM, IM, FM)
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		firePropertyChange("firstName",  this.firstName, this.firstName = firstName);
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		firePropertyChange("lastName", this.lastName, this.lastName = lastName);
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		firePropertyChange("city",  this.city, this.city = city);
	}

	public String getNation() {
		return nation;
	}

	public void setNation(String nation) {
		firePropertyChange("nation",  this.nation, this.nation = nation);
	}

	public int getFideCode() {
		return fideCode;
	}

	public void setFideCode(int fideCode) {
		firePropertyChange("fideCode",  this.fideCode, this.fideCode = fideCode);
	}

	public int getNationalCode() {
		return nationalCode;
	}

	public void setNationalCode(int nationalCode) {
		firePropertyChange("nationalCode",  this.nationalCode, this.nationalCode = nationalCode);
	}

	public int getElo() {
		return elo;
	}

	public void setElo(int elo) {
		firePropertyChange("elo",  this.elo, this.elo = elo);
	}

	public int getNationalElo() {
		return nationalElo;
	}

	public void setNationalElo(int nationalElo) {
		firePropertyChange("nationalElo",  this.nationalElo, this.nationalElo = nationalElo);
	}
		
	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public int getNationalCoefficient() {
		return nationalCoefficient;
	}

	public void setNationalCoefficient(int nationalCoefficient) {
		this.nationalCoefficient = nationalCoefficient;
	}

	public int getFideCoefficient() {
		return fideCoefficient;
	}

	public void setFideCoefficient(int fideCoefficient) {
		this.fideCoefficient = fideCoefficient;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String toString(){
		return firstName+" "+lastName;
	}
	
	public boolean equals(Object o){
		if(o==null){return false;}
		if(!(o instanceof Player)){return false;}
		Player other = (Player) o;
		if(this.getId()==other.getId()){
			return true;
		}
		
		return false;
	}
	
	@Override
	public Object clone() throws CloneNotSupportedException {
		Player clone = new Player();
		clone.id=0;
		clone.city=this.city;
		clone.elo=this.elo;
		clone.fideCode=this.fideCode;
		clone.firstName=this.firstName;
		clone.lastName=this.lastName;
		clone.nation=this.nation;
		clone.nationalCode=this.nationalCode;
		clone.nationalElo=this.nationalElo;
		clone.age=this.age;
		clone.category=this.category;
		clone.nationalCoefficient=this.nationalCoefficient;
		clone.fideCoefficient=this.fideCoefficient;
		return clone;
	}
}
