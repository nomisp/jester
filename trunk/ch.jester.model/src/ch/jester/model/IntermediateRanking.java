/**
 * 
 */
package ch.jester.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.OneToOne;

/**
 * Zwischenrangliste
 * @author Peter
 *
 */
@Entity
@DiscriminatorValue("IntermediateRanking")
public class IntermediateRanking extends Ranking {
	private static final long serialVersionUID = -7098601224169960670L;

	@OneToOne
	private Round round;
	
	public Round getRound() {
		return round;
	}

	public void setRound(Round round) {
		this.round = round;
	}

	/* (non-Javadoc)
	 * @see ch.jester.model.AbstractModelBean#clone()
	 */
	@Override
	public Object clone() throws CloneNotSupportedException {
		return createCompleteClone();
	}
}
