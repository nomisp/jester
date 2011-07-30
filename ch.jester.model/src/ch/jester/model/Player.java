package ch.jester.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.PreRemove;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlIDREF;
import javax.xml.bind.annotation.XmlList;


@Entity 
@Table(name = "Player")
@NamedQueries({
		@NamedQuery(name = Player.QUERY_GETALL, query = "SELECT player FROM Player player order by lastName, firstName"),
		@NamedQuery(name = Player.QUERY_GETALL_UNSORTED, query = "SELECT player FROM Player player"),
		@NamedQuery(name = Player.QUERY_COUNT, query = "SELECT count(player) FROM Player player"),
		@NamedQuery(name = Player.QUERY_FINDBYNAME, query = "SELECT player FROM Player player WHERE UPPER(player.lastName) LIKE :lastName order by lastName, firstName") ,
		@NamedQuery(name = Player.QUERY_FINDBYNAMESORCITY, query = "SELECT player FROM Player player WHERE UPPER(player.lastName) LIKE :para OR UPPER(player.firstName) LIKE :para OR UPPER(player.city) LIKE :para order by lastName, firstName, city") ,
		@NamedQuery(name = Player.QUERY_VALIDFIDECODE, query = "SELECT player FROM Player player WHERE player.fideCode > 0"),
		@NamedQuery(name = Player.QUERY_DUMMYPLAYER, query = "select p from Player p where p.firstName = 'Dummy' and p.lastName = 'Dummy'")})
public class Player extends AbstractModelBean<Player> {
	public final static String QUERY_GETALL = "Player.getAll";
	public final static String QUERY_GETALL_UNSORTED ="Player.getAllUnsorted";
	public final static String QUERY_COUNT ="Player.count";
	public final static String QUERY_FINDBYNAME ="Player.findByName";
	public final static String QUERY_FINDBYNAMESORCITY ="Player.findByNamesOrCity";
	public final static String QUERY_VALIDFIDECODE ="Player.findByFideCode";
	public final static String QUERY_DUMMYPLAYER = "FindDummyPlayer";
	
	private static final long serialVersionUID = -2351315088207630377L;

	@Column(name = "FirstName", nullable = false, length = 50)
	@NotNull
	private String firstName;

	@Column(name = "LastName", nullable = false, length = 50)
	@NotNull
	private String lastName;
	

	@Column(name = "City", nullable = true, length = 50)
	private String city;

	@Column(name = "Nation", nullable = true, length = 50)
	private String nation;

	@Column(name = "FideCode", nullable = true)
	private Integer fideCode;

	@Column(name = "NationalCode", nullable = true)
	private Integer nationalCode;

	@Column(name = "Elo", nullable = true)
	private Integer elo = 0;

	@Column(name = "NationalElo", nullable = true)
	private Integer nationalElo = 0;

	@Column(name = "EstimatedElo", nullable = true)
	private Integer estimatedElo = 0;

	@Column(name = "Age", nullable = true)
	private Integer age;

	@Column(name = "Category", nullable = true)
	private String category; // Kategorie eines Spielers z.B. Senior, Junior,
								// Schüler etc.

	@Column(name = "NationalCoefficient", nullable = true)
	private Integer nationalCoefficient; // Koeffizient zur Berechnung der
											// Nationalen Elo

	@Column(name = "FideCoefficient", nullable = true)
	private Integer fideCoefficient; // Koeffizient zur Berechnung der FIDE-Elo

	@Column(name = "Title", nullable = true)
	@Enumerated(EnumType.STRING)
	private Title title; // Titel eines Spielers (GM, IM, FM)


	@ManyToMany(cascade={CascadeType.PERSIST, CascadeType.MERGE})
	@JoinTable(
	      name="ClubPlayerAss",
	      joinColumns={@JoinColumn(name="PlayerId",referencedColumnName="Id")},
	      inverseJoinColumns={@JoinColumn(name="ClubId",referencedColumnName="Id")})
	private List<Club> clubs = new ArrayList<Club>();
	
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

	public Integer getEstimatedElo() {
		return estimatedElo;
	}

	public void setEstimatedElo(Integer estimatedElo) {
		this.estimatedElo = estimatedElo;
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

	public Title getTitle() {
		return title;
	}

	public void setTitle(Title title) {
		changeProperty("title", title);
	}
	@XmlList
	@XmlElement(name="ClubRef")
	@XmlIDREF
	public List<Club> getClubs() {
		return clubs;
	}

	public void setClubs(List<Club> clubs) {
		changeProperty("clubs", clubs);
	}
	
	public void addClub(Club club) {
		if (club == null) throw new IllegalArgumentException("club cannot be null");
		if (!clubs.contains(club)) {
			clubs.add(club);
			club.addPlayer(this); // Bidirektion
		}

	}
	
	public void removeClub(Club club) {
		if (club == null) throw new IllegalArgumentException("club cannot be null");
		boolean removed = clubs.remove(club);
		if(removed){
			club.removePlayer(this); // Bidirektion
		}
	}

	public String toString() {
		return firstName + " " + lastName;
	}
	
	@Override
	public boolean equalProperties(Player pOther) {
		if(this.fideCode!=null && pOther.fideCode!=null){
			if(this.fideCode==pOther.fideCode.intValue()&&this.fideCode>0 && pOther.fideCode>0){
				return true;
			}
		}
		if(this.nationalCode!=null && pOther.nationalCode!=null){
			if(this.nationalCode==pOther.nationalCode.intValue() && this.nationalCode>0 && pOther.nationalCode>0){
				return true;
			}
		}
		return false;
	}

	@PreRemove
	public void preRemove(){
		int s = getClubs().size();
		for(int i=0;i<s;i++){
			getClubs().get(i).removePlayer(this);
		}
	}
	
	@Override
	public Object clone() throws CloneNotSupportedException {
		Player clone = cloneWithSimpleProperties();
		for(Club c:this.getClubs()){
			clone.addClub(c);
		}
		return clone;
	}


}
