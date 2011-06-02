package ch.jester.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "Player")
@NamedQueries({
		@NamedQuery(name = Player.QUERY_GETALL, query = "SELECT player FROM Player player order by lastName, firstName"),
		@NamedQuery(name = Player.QUERY_GETALL_UNSORTED, query = "SELECT player FROM Player player"),
		@NamedQuery(name = Player.QUERY_COUNT, query = "SELECT count(player) FROM Player player"),
		@NamedQuery(name = Player.QUERY_FINDBYNAME, query = "SELECT player FROM Player player WHERE UPPER(player.lastName) LIKE :lastName order by lastName, firstName") ,
		@NamedQuery(name = Player.QUERY_VALIDFIDECODE, query = "SELECT player FROM Player player WHERE player.fideCode > 0") })
public class Player extends AbstractModelBean<Player> {
	public final static String QUERY_GETALL = "Player.getAll";
	public final static String QUERY_GETALL_UNSORTED ="Player.getAllUnsorted";
	public final static String QUERY_COUNT ="Player.count";
	public final static String QUERY_FINDBYNAME ="Player.findByName";
	public final static String QUERY_VALIDFIDECODE ="Player.findByFideCode";
	
	private static final long serialVersionUID = -2351315088207630377L;

	@Column(name = "FirstName", nullable = false, length = 50)
	// @org.hibernate.validator.constraints.Length(min=2, max=50)
	@NotNull
	private String firstName;

	@Column(name = "LastName", nullable = false, length = 50)
	// @org.hibernate.validator.constraints.Length(min=2, max=50)
	@NotNull
	private String lastName;
	

	@Column(name = "City", nullable = true, length = 50)
	private String city;

	@Column(name = "Nation", nullable = true, length = 50)
	private String nation;

	@org.hibernate.annotations.Index(name = "IDX_FIDEINDEX")
	@Column(name = "FideCode", nullable = true)
	private Integer fideCode;

	@Column(name = "NationalCode", nullable = true)
	private Integer nationalCode;

	@Column(name = "Elo", nullable = true)
	private Integer elo;

	@Column(name = "NationalElo", nullable = true)
	private Integer nationalElo;

	@Column(name = "Age", nullable = true)
	private Integer age;

	@Column(name = "Category", nullable = true)
	private String category; // Kategorie eines Spielers z.B. Senior, Junior,
								// Sch�ler etc.

	@Column(name = "NationalCoefficient", nullable = true)
	private Integer nationalCoefficient; // Koeffizient zur Berechnung der
											// Nationalen Elo

	@Column(name = "FideCoefficient", nullable = true)
	private Integer fideCoefficient; // Koeffizient zur Berechnung der FIDE-Elo

	@Column(name = "Title", nullable = true)
	private String title; // Titel eines Spielers (GM, IM, FM)

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		changeProperty("firstName", firstName);
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		changeProperty("lastName", lastName);
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		changeProperty("city", city);
	}

	public String getNation() {
		return nation;
	}

	public void setNation(String nation) {
		changeProperty("nation", nation);
	}

	public Integer getFideCode() {
		return fideCode;
	}

	public void setFideCode(Integer fideCode) {
		changeProperty("fideCode", fideCode);
	}

	public Integer getNationalCode() {
		return nationalCode;
	}

	public void setNationalCode(Integer nationalCode) {
		changeProperty("nationalCode", nationalCode);
	}

	public Integer getElo() {
		return elo;
	}

	public void setElo(Integer elo) {
		changeProperty("elo", elo);
	}

	public Integer getNationalElo() {
		return nationalElo;
	}

	public void setNationalElo(Integer nationalElo) {
		changeProperty("nationalElo", nationalElo);
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		changeProperty("age", age);
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		changeProperty("category", category);
	}

	public Integer getNationalCoefficient() {
		return nationalCoefficient;
	}

	public void setNationalCoefficient(Integer nationalCoefficient) {
		changeProperty("nationalCoefficient", nationalCoefficient);
	}

	public Integer getFideCoefficient() {
		return fideCoefficient;
	}

	public void setFideCoefficient(Integer fideCoefficient) {
		changeProperty("fideCoefficient", fideCoefficient);
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		changeProperty("title", title);
	}

	public String toString() {
		return firstName + " " + lastName;
	}
	

	@Override
	public Object clone() throws CloneNotSupportedException {
		return createCompleteClone();
	}


}
