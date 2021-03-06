package ch.jester.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlTransient;

/**
 * Verein
 *
 */
@Entity
@Table(name="Club")
@NamedQueries({
	@NamedQuery(name=Club.QUERY_GETCLUBBYNAME, query="select c from Club c where c.name = :name order by c.name"),
	@NamedQuery(name=Club.QUERY_GETALLCLUBS, query="select c from Club c"),
	@NamedQuery(name=Club.QUERY_COUNTCLUBS, query="SELECT count(club) FROM Club club")
})
public class Club extends AbstractModelBean<Club> {
	private static final long serialVersionUID = 3749001233544554089L;
	public final static String QUERY_GETCLUBBYNAME = "Club.getclubbyname";
	public final static String QUERY_GETALLCLUBS = "Club.getAll";
	public final static String QUERY_COUNTCLUBS = "Club.count";
	@Column(name="Name", nullable=false, unique=true, length=50)
	@NotNull
	private String name;
	
	@Column(name="Code", nullable=true)
	private Integer code;
	
	@ManyToMany(mappedBy="clubs", cascade={CascadeType.PERSIST, CascadeType.MERGE})
	
	private List<Player> players = new ArrayList<Player>();

	@XmlAttribute
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	@XmlAttribute
	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	@XmlTransient
	public List<Player> getPlayers() {
		return players;
	}

	public void setPlayers(List<Player> players) {
		this.players = players;
	}
	
	public void addPlayer(Player player) {
		if (player == null) throw new IllegalArgumentException("player cannot be null");
		if (!players.contains(player)){
			players.add(player);
			player.addClub(this); // Bidirektion
		}
		
	}
	@Override
	public boolean equalProperties(Club pOther) {
		return name.equals(pOther.name);
	}
	
	public void removePlayer(Player player) {
		if (player == null) throw new IllegalArgumentException("player cannot be null");
		boolean removed = players.remove(player);
		if(removed){
			player.removeClub(this); // Bidirektion
		}
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return null;
	}
	public void afterUnmarshal(Unmarshaller u, Object parent) {
		if(parent instanceof Tournament){return;}
	    addPlayer((Player) parent);
}
}
