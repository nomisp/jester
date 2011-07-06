package ch.jester.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name="Pairing")
@NamedQueries({
	@NamedQuery(name="AllPairings", query="select p from Pairing p"),
	@NamedQuery(name="PairingsByPlayerCard", query="select p from Pairing p where :player = p.white or :player = p.black"),
	@NamedQuery(name="PairingsByPlayerCardAndCategory", query="select p from Pairing p where :player = p.white or :player = p.black and p.round.category = :category"),
	@NamedQuery(name="PairingsByPlayerCardAndTournament", query="select p from Pairing p where :player = p.white or :player = p.black and p.round.category.tournament = :tournament")
})
public class Pairing extends AbstractModelBean<Pairing> {
	private static final long serialVersionUID = -5183089936204591328L;
	
	@OneToOne(cascade={CascadeType.PERSIST, CascadeType.MERGE})
	@JoinColumn(name = "WhiteId", referencedColumnName = "Id")
	private PlayerCard white;
	
	@OneToOne(cascade={CascadeType.PERSIST, CascadeType.MERGE})
	@JoinColumn(name = "BlackId", referencedColumnName = "Id")
	private PlayerCard black;
	
	@Column(name="Result")
	private String result;
	
	@ManyToOne
	private Round round;


	public PlayerCard getWhite() {
		return white;
	}

	public void setWhite(PlayerCard white) {
		this.white = white;
	}

	public PlayerCard getBlack() {
		return black;
	}

	public void setBlack(PlayerCard black) {
		this.black = black;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public Round getRound() {
		return round;
	}

	public void setRound(Round round) {
		if (round == null) throw new IllegalArgumentException("round cannot be null");
		this.round = round;
		this.round.addPairing(this);	// Bidirektionale Beziehung
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		return createCompleteClone();
	}
	
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(white.toString());
		sb.append(" vs ");
		sb.append(black.toString());
		return sb.toString();
	}
}
