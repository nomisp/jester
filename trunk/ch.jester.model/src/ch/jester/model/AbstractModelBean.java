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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlTransient;

import org.eclipse.core.runtime.Assert;

import ch.jester.commonservices.api.adaptable.IHierarchyAdapter;
import ch.jester.commonservices.api.persistency.IEntityObject;

@MappedSuperclass
public abstract class AbstractModelBean<T extends IEntityObject> extends AbstractPropertyChangeModel implements Cloneable, Serializable , IEntityObject, IHierarchyAdapter{
	private static final long serialVersionUID = 1L;
	
	@Transient
	private String pXmlSerialId;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	protected Integer id;
	
	@XmlAttribute(name="id")
	@XmlID
	public String getSerialId(){
		if(pXmlSerialId==null){
			pXmlSerialId = UUID.randomUUID().toString();
		}
		return pXmlSerialId;
	}
	@XmlTransient
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		if(id!=null){
			Assert.isLegal(id!=0);
		}
		this.id = id;
	}
	
	public boolean isUnsafed(){
		return id==null||id==0;
	}
	
	/**
	 * Regeln:<br>
	 * - Referenzvergleich, wenn beide Objekte id 0 haben<br>
	 * - sonst this.id == other.id
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public boolean equals(Object o){
		if(o==null){return false;}
		if (!(this.getClass().isInstance(o))) return false;
		AbstractModelBean other = (AbstractModelBean) o;
		//beide unsafed
		if(this.isUnsafed()&&other.isUnsafed()){
			return this==other;
		}
		//nur einer unsafed
		if(this.id==null&&other.id!=null || this.id!=null && other.id==null){
			return equalProperties((T)other);
		}else{
			
			return this.id.intValue()==other.id.intValue();
		}
		/*if(pE){
			return pE;
		}*/
		
/*		if(this.id==0 && other.id == 0){
			return this==other;
		}
		return this.id.intValue()==other.id.intValue();*/
	}
	
	/**Bereits überprüft wurden:<br>
	 * - null <br>
	 * - Klasse <br>
	 * - id <br>
	 * @param pOther
	 * @return
	 */
	public boolean equalProperties(T pOther){
		return false;
	}
	
	/**Erzeugt neue Instanz und setzt ID auf 0;
	 * AbstractModelBean muss dafür typisiert sein!!
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected T createCloneId0(){
		Type actualTypeArgument = ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
		try {
			T t = ((Class<T>) actualTypeArgument).newInstance();
			t.setId(null);
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
		o.setId(null);
		return o;
	}
	
	@SuppressWarnings("unchecked")
	protected T cloneWithSimpleProperties(Class<?>... clzs){
		T o = createCloneId0();
		cloneFields((T)this, o, clzs);
		o.setId(null);
		return o;
	}
	protected T cloneWithSimpleProperties(){
		T o = cloneWithSimpleProperties(String.class, Integer.class, Boolean.class, Double.class, Float.class);
		o.setId(null);
		return o;
	}
	protected void cloneFields(T original, T clone) {
		cloneFields(original, clone, new Class[]{});
	}
	/**
	 * Kopiert Properties vom Original in den Klon, wenn <br>
	 * - eine getter und setter Methode vorhanden ist
	 * @param original
	 * @param clone
	 */
	protected void cloneFields(T original, T clone, Class<?>... clzs) {
		List<Class<?>> classList = Arrays.asList(clzs);
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
			if(!classList.isEmpty()){
				if(!classList.contains(readMethod.getReturnType())){
					continue;
				}
			}
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
	@SuppressWarnings({ "unchecked"})
	public <V> Collection<V> getChildrenCollection(Class<V> clz){
		if(clz==AbstractModelBean.this.getClass()){
			List<Object> list = new ArrayList<Object>();
			list.add(this);
			return (Collection<V>) list;
		}
		return null;
	}
	
	@Override
	public <V> boolean canGetChildrenCollection(Class<V> clz) {
		if(clz==AbstractModelBean.this.getClass()){
			return true;
		}
		return false;
	}
	public abstract Object clone() throws CloneNotSupportedException;
}
