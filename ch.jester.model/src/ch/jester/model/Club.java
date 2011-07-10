package ch.jester.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name="Club")
public class Club extends AbstractModelBean<Club> {
	private static final long serialVersionUID = 3749001233544554089L;
	
	@Column(name="Name", nullable=false, unique=true, length=50)
	//@org.hibernate.validator.constraints.Length(min=2, max=50)
	@NotNull
	private String name;
	
	@Column(name="Code", nullable=true)
	private Integer code;
	
	@ManyToMany
	private List<Player> players = new ArrayList<Player>();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public List<Player> getPlayers() {
		return players;
	}

	public void setPlayers(List<Player> players) {
		this.players = players;
	}
	
	public void addPlayer(Player player) {
		if (player == null) throw new IllegalArgumentException("player cannot be null");
		if (!players.contains(player)) players.add(player);
		player.addClub(this); // Bidirektion
	}
	
	public void removePlayer(Player player) {
		if (player == null) throw new IllegalArgumentException("player cannot be null");
		players.remove(player);
		player.removeClub(this); // Bidirektion
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return null;
	}
}
