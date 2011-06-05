package ch.jester.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name="Pairing")
public class Pairing extends AbstractModelBean<Pairing> {
	private static final long serialVersionUID = -5183089936204591328L;
	
	@OneToOne
	@JoinColumn(name = "WhiteId", referencedColumnName = "Id")
	private Player white;
	
	@OneToOne
	@JoinColumn(name = "BlackId", referencedColumnName = "Id")
	private Player black;
	
	@Column(name="Result")
	private String result;
	
	@ManyToOne
	private Round round;


	public Player getWhite() {
		return white;
	}

	public void setWhite(Player white) {
		this.white = white;
	}

	public Player getBlack() {
		return black;
	}

	public void setBlack(Player black) {
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
