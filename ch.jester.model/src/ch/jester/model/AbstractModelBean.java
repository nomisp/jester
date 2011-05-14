package ch.jester.model;

import java.io.Serializable;

import ch.jester.common.model.AbstractPropertyChangeModel;
import ch.jester.commonservices.api.persistency.IDaoObject;

public abstract class AbstractModelBean extends AbstractPropertyChangeModel implements Cloneable, Serializable , IDaoObject{
	private static final long serialVersionUID = 1L;

	/**
	 * Regeln:<br>
	 * - Referenzvergleich, wenn beide Objekte id 0 haben<br>
	 * - sonst this.id == other.id
	 */
	public boolean equals(Object o){
		if(o==null){return false;}
		if (!(this.getClass().isInstance(o))) return false;
		AbstractModelBean other = (AbstractModelBean) o;
		if(this.getId()==0&&other.getId()==0){
			return this==other;
		}
		return this.getId()==other.getId();
	}
	
	public abstract Object clone() throws CloneNotSupportedException;
}
