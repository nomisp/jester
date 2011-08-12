/**
 * 
 */
package ch.jester.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlTransient;

/**
 * Entität für die Feinwertungssysteme.
 * Eigentlich nur ein Wrapper, damit ein Turnier mehrere Feinwertungssysteme haben kann.
 *
 */
@Entity
@Table(name="RankingSystem")
public class RankingSystem extends AbstractModelBean<RankingSystem> {
	private static final long serialVersionUID = -1372443370449024500L;

	@Column(name="PluginId", nullable=false)
	private String pluginId;

	@Column(name="ImplementationClass", nullable=false)
	private String implementationClass; // Feinwertung (als deklarativer Service implementiert) entspricht dem EP-Attribut: class
	
	@Column(name="ShortType", nullable=false)
	private String shortType;
	
	@Column(name="Description", nullable=true)
	private String description;
	
	/**
	 * Nummer für die Reihenfolge in der die Feinwertungssysteme beim erstellen der
	 * Rangliste angewandt werden.
	 */
	@Column(name="RankingSystemNumber", nullable=false)
	private Integer rankingSystemNumber;
	
	@ManyToOne

	private Tournament tournament;
	
	public RankingSystem() {
		
	}
	
	public String getPluginId() {
		return pluginId;
	}

	public void setPluginId(String pluginId) {
		this.pluginId = pluginId;
	}

	public String getImplementationClass() {
		return implementationClass;
	}

	public void setImplementationClass(String implementationClass) {
		this.implementationClass = implementationClass;
	}

	public String getShortType() {
		return shortType;
	}

	public void setShortType(String shortType) {
		this.shortType = shortType;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getRankingSystemNumber() {
		return rankingSystemNumber;
	}

	public void setRankingSystemNumber(Integer rankingSystemNumber) {
		this.rankingSystemNumber = rankingSystemNumber;
	}
	@XmlTransient
	public Tournament getTournament() {
		return tournament;
	}

	public void setTournament(Tournament tournament) {
		this.tournament = tournament;
		tournament.addRankingSystem(this); // Bidirektion
	}

	/* (non-Javadoc)
	 * @see ch.jester.model.AbstractModelBean#clone()
	 */
	@Override
	public Object clone() throws CloneNotSupportedException {
		//RankingSystem rsclone = cloneWithSimpleProperties();
		
		return createCompleteClone();
	}
	public void afterUnmarshal(Unmarshaller u, Object parent) {
	    this.tournament = (Tournament)parent;
}
}
