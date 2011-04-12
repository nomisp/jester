package ch.jester.model;

import java.io.Serializable;

import ch.jester.common.model.AbstractPropertyChangeModel;
import ch.jester.dao.IDAO;

public abstract class AbstractModelBean extends AbstractPropertyChangeModel implements Cloneable, Serializable , IDAO{

	public abstract Object clone() throws CloneNotSupportedException;
	public boolean equals(Object o){
		if(o==null){return false;}
		if(!(o instanceof AbstractModelBean)){return false;}
		AbstractModelBean other = (AbstractModelBean) o;
		if(this.getId()==other.getId()){
			return true;
		}
		
		return false;
	}
}
