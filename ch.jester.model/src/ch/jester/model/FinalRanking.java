/**
 * 
 */
package ch.jester.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.PreRemove;

/**
 * Finale Rangliste
 *
 */
@Entity
@DiscriminatorValue("FinalRanking")
public class FinalRanking extends Ranking {
	private static final long serialVersionUID = -3871911017932874564L;

	@OneToOne(mappedBy="ranking")
	private Category category;
	
	@PreRemove
	public void preRemove(){
		this.category.setRanking(null);
		super.preRemove();
	}

	
	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	/* (non-Javadoc)
	 * @see ch.jester.model.AbstractModelBean#clone()
	 */
	@Override
	public Object clone() throws CloneNotSupportedException {
		return createCompleteClone();
	}
}
