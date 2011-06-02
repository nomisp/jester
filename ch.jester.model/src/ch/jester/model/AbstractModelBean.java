package ch.jester.model;

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

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlTransient;

import ch.jester.common.model.AbstractPropertyChangeModel;
import ch.jester.commonservices.api.persistency.IEntityObject;

@MappedSuperclass
public abstract class AbstractModelBean<T extends IEntityObject> extends AbstractPropertyChangeModel implements Cloneable, Serializable , IEntityObject{
	private static final long serialVersionUID = 1L;

	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	protected int id;
	
	@XmlAttribute(name="id")
	@XmlID
	public String getSerialId(){
		return id+"";
	}
	@XmlTransient
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
	
	/**Erzeugt einen kompletten Klon, mit Id 0<br>
	 * Referenzen bleiben unverändert!
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
	 * Kopiert Properties vom Original in den Klon, wenn <br>
	 * - eine getter und setter Methode vorhanden ist
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
	/**
	 * Ändert das Property auf den übergebenen Wert und feuert den 
	 * PropertyChange.<br>
	 * Achtung es wird direkt auf das Feld zugegriffen.
	 * Feldname muss daher gleich propertyId sein.
	 * @param propertyId
	 * @param value
	 */
	public void changeProperty(String propertyId, Object value){
		try {
			Field field = AbstractModelBean.this.getClass().getDeclaredField(propertyId);
			field.setAccessible(true);
			Object oldvalue = field.get(AbstractModelBean.this);
			field.set(AbstractModelBean.this, value);
			firePropertyChange(propertyId, oldvalue, value);
			
			
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public abstract Object clone() throws CloneNotSupportedException;
}
