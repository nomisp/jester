package ch.jester.model;

import java.beans.BeanDescriptor;
import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Enumeration;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import ch.jester.common.model.AbstractPropertyChangeModel;
import ch.jester.commonservices.api.persistency.IDaoObject;

@MappedSuperclass
public abstract class AbstractModelBean<T extends IDaoObject> extends AbstractPropertyChangeModel implements Cloneable, Serializable , IDaoObject{
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
	
	/**Erzeugt neue Instanz und setzt ID auf 0;
	 * AbstractModelBean muss dafür typisiert sein!!
	 * @return
	 */
	protected T createCloneId0(){
		Type actualTypeArgument = ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
		try {
			T t = ((Class<T>) actualTypeArgument).newInstance();
			t.setId(0);
			return t;
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	/**Erzeugt einen kompletten Klone, mit Id 0
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected T createCompleteClone(){
		T o =  createCloneId0();
		cloneFields((T) this, o);
		o.setId(0);
		return o;
	}
	
	/**
	 * Kopiert properties vom Originial in den Klon
	 * @param original
	 * @param clone
	 */
	protected void cloneFields(T original, T clone) {
		BeanInfo originalInfo = null;
		try {
			originalInfo = Introspector.getBeanInfo(this.getClass());
		} catch (IntrospectionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		PropertyDescriptor[] descriptor = originalInfo.getPropertyDescriptors();
		

		for(PropertyDescriptor d: descriptor){
			Method writemethod = d.getWriteMethod();
			if(writemethod==null){continue;}
			Method readMethod = d.getReadMethod();
			if(readMethod==null){continue;}
			try {
				Object origValue = readMethod.invoke(original, new Object[]{});
				writemethod.invoke(clone, origValue);
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
	public abstract Object clone() throws CloneNotSupportedException;
}
