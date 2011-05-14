package ch.jester.model;

import java.io.Serializable;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import ch.jester.common.model.AbstractPropertyChangeModel;
import ch.jester.commonservices.api.persistency.IDaoObject;

@MappedSuperclass
public abstract class AbstractModelBean extends AbstractPropertyChangeModel implements Cloneable, Serializable , IDaoObject{
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	protected int id;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
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
