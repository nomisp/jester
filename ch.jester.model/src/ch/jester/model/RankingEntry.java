package ch.jester.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * Position in der Rangliste
 * @author Peter
 *
 */
@Entity
@Table(name="RankingEntry")
public class RankingEntry extends AbstractModelBean<RankingEntry> {
	private static final long serialVersionUID = 1233271455914836718L;

	@ManyToOne
	private Ranking ranking;
	
	@OneToOne
	private PlayerCard playerCard;
	
	@Column(name="Position")
	private Integer position;
	
	public Ranking getRanking() {
		return ranking;
	}

	public void setRanking(Ranking ranking) {
		this.ranking = ranking;
	}

	public PlayerCard getPlayerCard() {
		return playerCard;
	}

	public void setPlayerCard(PlayerCard playerCard) {
		this.playerCard = playerCard;
	}

	public Integer getPosition() {
		return position;
	}

	public void setPosition(Integer position) {
		this.position = position;
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		return createCompleteClone();
	}

}
