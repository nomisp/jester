package ch.jester.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlIDREF;

/**
 * Spielerkarte um einem Spieler für das Turnier relevante Daten
 * wie zum Beispiel seine Startnummer anzuhängen.
 * 
 * @author Peter
 *
 */
@Entity
@Table(name="PlayerCard")
public class PlayerCard extends AbstractModelBean<PlayerCard> {
	private static final long serialVersionUID = -2710264494286525315L;
	
	@OneToOne(cascade={CascadeType.PERSIST})
	@JoinTable(name="PlayerCardPlayerAss",
				joinColumns = {@JoinColumn(name = "PlayerCardId")},
				inverseJoinColumns = {@JoinColumn(name = "PlayerId")})
	private Player player;

	@ManyToOne
	private Category category;
	
	@Column
	private Integer number;
	
	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		changeProperty("player", player);
	}

	public Category getCategory() {
		return category;
	}

	@XmlIDREF
	@XmlAttribute(name="categoryRef")
	public void setCategory(Category category) {
		changeProperty("category", category);
	}

	public Integer getNumber() {
		return number;
	}

	public void setNumber(Integer number) {
		changeProperty("number", number);
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		return createCompleteClone();
	}

}
