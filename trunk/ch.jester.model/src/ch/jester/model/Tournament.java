package ch.jester.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlTransient;

/**
 * Entität für die Tabelle Tournament 
 *
 */
@Entity
@Table(name="Tournament")
@NamedQueries({
	@NamedQuery(name="AllTournaments", query="select t from Tournament t order by t.name, t.dateFrom"),
	@NamedQuery(name=Tournament.QUERY_GETALL, query="select t from Tournament t order by t.name, t.dateFrom"),
	@NamedQuery(name=Tournament.QUERY_GETALLACTIVE, query="select t from Tournament t where t.active = true order by t.name, t.dateFrom"),
	@NamedQuery(name="countTournaments",query="SELECT count(Tournament) FROM Tournament"),
	@NamedQuery(name="TournamentByName", query="select t from Tournament t where t.name like :name")
})
public class Tournament extends AbstractModelBean<Tournament> {
	private static final long serialVersionUID = -3356578830307874396L;
	public final static String QUERY_GETALLACTIVE = "AllActiveTournaments";
	public final static String QUERY_GETALL= "Tournament.getAll";
	private transient Object rootElement;	// Rootelement für CNF (Common Navigator Framework) Viewer

	@Column(name="Name", nullable=false, length=50)
	//@org.hibernate.validator.constraints.Length(min=2, max=50)
	@NotNull
	private String name;
	
	@Column(name="Description", nullable=true, length=255)
	private String description;
	
	@Column(name="Year", nullable=true)
	private int year;
	
	@Column(name="DateFrom", nullable=true)
	@Temporal(TemporalType.DATE)
	private Date dateFrom;
	
	@Column(name="DateTo", nullable=true)
	@Temporal(TemporalType.DATE)
	private Date dateTo;
	
	@Column(name="PairingSystemPlugin", nullable=false)
	private String pairingSystemPlugin;	// Plugin-Id des PairingSystems um Eindeutigkeit der Implementierungsklasse zu garantieren
	
	@Column(name="PairingSystem", nullable=false)
	private String pairingSystem; // Paarungssystem (als deklarativer Service implementiert) entspricht dem EP-Attribut: class
	
	@Column(name="SettingsPage", nullable=true)
	private String settingsPage; // FormPage-Klasse für die Benutzereinstellungen
	
	@OneToMany(mappedBy="tournament", cascade=CascadeType.ALL, orphanRemoval=true)
	private List<RankingSystem> rankingSystems = new ArrayList<RankingSystem>();
	
	@Column(name="EloCalculator", nullable=false)
	private String eloCalculator; // EloCalculator (als deklarativer Service implementiert) entspricht dem EP-Attribut: class
	
	@OneToMany(mappedBy="tournament", cascade=CascadeType.ALL, orphanRemoval=true)
	private List<Category> categories = new ArrayList<Category>();

	@Column(name="Active")
	private Boolean active;
	
	@Column(name="Started")
	private Boolean started;
	
	@OneToOne(optional=true, cascade=CascadeType.ALL, orphanRemoval=true)
	private SettingItem settingItem;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		changeProperty("name", name);
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		changeProperty("description", description);
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		changeProperty("year", year);
	}

	public Date getDateFrom() {
		return dateFrom;
	}

	public void setDateFrom(Date dateFrom) {
		changeProperty("dateFrom", dateFrom);
	}

	public Date getDateTo() {
		return dateTo;
	}

	public void setDateTo(Date dateTo) {
		changeProperty("dateTo", dateTo);
	}

	public String getPairingSystemPlugin() {
		return pairingSystemPlugin;
	}

	public void setPairingSystemPlugin(String pairingSystemPlugin) {
		this.pairingSystemPlugin = pairingSystemPlugin;
	}

	public String getPairingSystem() {
		return pairingSystem;
	}

	public void setPairingSystem(String pairingSystem) {
		this.pairingSystem = pairingSystem;
	}

	public String getSettingsPage() {
		return settingsPage;
	}

	public void setSettingsPage(String settingsPage) {
		this.settingsPage = settingsPage;
	}

	public String getEloCalculator() {
		return eloCalculator;
	}

	public void setEloCalculator(String eloCalculator) {
		this.eloCalculator = eloCalculator;
	}


	public List<Category> getCategories() {
		return categories;
	}

	public void setCategories(List<Category> categories) {
		for (Category category : categories) {
			category.setTournament(this);
		}
		this.categories = categories;
	}
	
	public void addCategory(Category cat) {
		if (cat == null) throw new IllegalArgumentException("category may not be null");
		if (!this.categories.contains(cat)) this.categories.add(cat);
		cat.setTournament(this);
	}
	
	public void removeCategory(Category cat) {
		if (cat == null) throw new IllegalArgumentException("category may not be null");
		this.categories.remove(cat);
		cat.setTournament(null);
	}
	
	public Object getRootElement() {
		return rootElement;
	}

	public void setRootElement(Object rootElement) {
		this.rootElement = rootElement;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public Boolean getStarted() {
		return started;
	}

	public void setStarted(Boolean started) {
		this.started = started;
	}

	public SettingItem getSettingItem() {
		return settingItem;
	}

	public void setSettingItem(SettingItem settingItem) {
		this.settingItem = settingItem;
		// Bidirektion
		if (settingItem.getTournament() != this) settingItem.setTournament(this);
	}

	public List<RankingSystem> getRankingSystems() {
		return rankingSystems;
	}

	public void setRankingSystems(List<RankingSystem> rankingSystems) {
		this.rankingSystems = rankingSystems;
	}
	
	public void addRankingSystem(RankingSystem rankingSystem) {
		if (rankingSystem == null) throw new IllegalArgumentException("rankingSystem can not be null");
		if (!rankingSystems.contains(rankingSystem)) rankingSystems.add(rankingSystem);
		if (rankingSystem.getTournament() != this) rankingSystem.setTournament(this); // Bidirektion
	}
	
	/**
	 * Liefert die primäre Feinwertung (kleinste Nummer)
	 * @return
	 */
	public RankingSystem getPrimaryRankingSystem() {
		RankingSystem primaryRankingSystem = rankingSystems.get(0);
		for (RankingSystem rs : rankingSystems) {
			if (primaryRankingSystem.getRankingSystemNumber() > rs.getRankingSystemNumber()) {
				primaryRankingSystem = rs;
			}
		}
		return primaryRankingSystem;
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
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result
				+ ((pairingSystem == null) ? 0 : pairingSystem.hashCode());
		result = prime * result + year;
		return result;
	}
	
	@Override
	public Object clone() throws CloneNotSupportedException {
		Tournament clone = super.cloneWithSimpleProperties(String.class, Integer.class, Boolean.class);
		List<Category> clist= new ArrayList<Category>();
		for(Category cat:this.getCategories()){
			Category catClone = (Category) cat.clone();
			clist.add(catClone);
		}
		clone.setCategories(clist);
		
		List<RankingSystem> rlist= new ArrayList<RankingSystem>();
		for(RankingSystem r:this.getRankingSystems()){
			RankingSystem rclone = (RankingSystem) r.clone();
			clone.addRankingSystem(rclone);
		}
		clone.setDateFrom((Date) this.dateFrom.clone());
		clone.setDateTo((Date) this.dateTo.clone());
		clone.setRankingSystems(rlist);
		return clone;
	}
	@XmlTransient
	public List<Category> getPairedCategories(){
		List<Category> paired = new ArrayList<Category>();
		for(Category c:getCategories()){
			if(c.isPaired()){
				paired.add(c);
			}
		}
		return paired;
	}
	
	public boolean isPaired(){
		return getPairedCategories().size()>0;
	}
	
	@XmlElementWrapper(name = "PlayerList")
	@XmlElement(name = "Player")
	public List<Player> getPlayers(){
		List<Player> playerlist = new ArrayList<Player>();
		for(Category cat:getCategories()){
			playerlist.addAll(cat.getPlayers());
		}
		return playerlist;
	}
	@XmlElementWrapper(name = "ClubList")
	@XmlElement(name = "Club")
	public List<Club> getClubs(){
		List<Club> clublist = new ArrayList<Club>();
		List<Player> plist = getPlayers();
		for(Player p:plist){
			if(p==null){continue;}
			List<Club> playerclubs = p.getClubs();
			for(Club c:playerclubs){
				if(!clublist.contains(c)){
					clublist.add(c);
				}
			}
		}
		
		return clublist;
		
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> Collection<T> getChildrenCollection(Class<T> clz) {
		if(clz==Player.class){
			return (Collection<T>) getPlayers();
		}
		if(clz==Category.class){
			return (Collection<T>) getCategories();
		}
		return super.getChildrenCollection(clz);
	}
	
	@Override
	public <T> boolean canGetChildrenCollection(Class<T> clz) {
		if(clz==Player.class || clz==Category.class){
			return true;
		}
		return super.canGetChildrenCollection(clz);
	}
	
}
