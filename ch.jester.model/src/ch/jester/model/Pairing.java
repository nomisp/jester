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
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlIDREF;
import javax.xml.bind.annotation.XmlType;

import ch.jester.model.util.Result;

@Entity
@Table(name="Pairing")
@NamedQueries({
	@NamedQuery(name="AllPairings", query="select p from Pairing p"),
	@NamedQuery(name="PairingsByPlayerCard", query="select p from Pairing p where :player = p.white or :player = p.black"),
	@NamedQuery(name="PairingsByPlayerCardAndCategory", query="select p from Pairing p where :player = p.white or :player = p.black and p.round.category = :category"),
	@NamedQuery(name="PairingsByPlayerCardAndTournament", query="select p from Pairing p where :player = p.white or :player = p.black and p.round.category.tournament = :tournament")
})
@XmlType(propOrder={"white","black","result","round"})
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
	
	@Column
	private Integer number;

	/*@PreRemove
	public void preRemove(){
		round.removePairing(this);
		
		
	}*/

	@XmlIDREF
	@XmlElement(name="whiteRef")
	public PlayerCard getWhite() {
		return white;
	}

	public void setWhite(PlayerCard white) {
		this.white = white;
	}

	@XmlIDREF
	@XmlElement(name="blackRef")
	public PlayerCard getBlack() {
		return black;
	}

	public void setBlack(PlayerCard black) {
		this.black = black;
	}

	@XmlElement(name="result")
	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		//unmarshalling problem hier.
		//pc white und black werden nicht miteinander gesetzt.
		//daher gibts silent npe... sehr geschickt.
		if(this.result!=null){
			//modify result
			Result oldResult = Result.findByShortResult(this.result);
			Result newResult = Result.findByShortResult(result);
			this.getBlack().modifyResult(oldResult.getPointsBlack(), newResult.getPointsBlack(), round.getNumber());
			this.getWhite().modifyResult(oldResult.getPointsWhite(), newResult.getPointsWhite(), round.getNumber());
			
		} else {
			Result r = Result.findByShortResult(result);
			Double blackpoints = r.getPointsBlack();
			this.getBlack().addResult(blackpoints);
			
			Double whitepoints = r.getPointsWhite();
			this.getWhite().addResult(whitepoints);
		}
		this.result = result;
	}

	@XmlIDREF
	@XmlAttribute(name="roundRef")
	public Round getRound() {
		return round;
	}

	public void setRound(Round round) {
		if (round == null) throw new IllegalArgumentException("round cannot be null");
		this.round = round;
		if(!this.round.getPairings().contains(this)){
			this.round.addPairing(this);	// Bidirektionale Beziehung
		}
	}

	@XmlAttribute
	public Integer getNumber() {
		return number;
	}

	public void setNumber(Integer number) {
		this.number = number;
	}

//	public void afterUnmarshal(Unmarshaller u, Object parent) {
	 //   this.round = (Round)parent;
 //}
	@Override
	public Object clone() throws CloneNotSupportedException {
		return createCompleteClone();
	}
	
	public String toString() {
		StringBuffer sb = new StringBuffer();
		if(white!=null){
			sb.append(white.toString());
		}else{
			sb.append("?");
		}
		sb.append(" vs ");
		if(black!=null){
			sb.append(black.toString());
		}else{
			sb.append("?");
		}
		return sb.toString();
	}
}
