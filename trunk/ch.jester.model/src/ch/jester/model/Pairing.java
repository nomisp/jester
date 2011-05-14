package ch.jester.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name="Pairing")
public class Pairing extends AbstractModelBean {
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
		this.round = round;
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return null;
	}
}
