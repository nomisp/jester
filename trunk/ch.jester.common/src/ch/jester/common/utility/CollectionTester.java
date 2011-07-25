package ch.jester.common.utility;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.Platform;

import ch.jester.commonservices.api.adaptable.IHierarchyAdapter;

/**
 * Utility Klasse
 * Testet Kollections auf ihren Inhalt unter Einbezug von IAdaptable und IHierarchyAdapter
 *
 */
public class CollectionTester {
	
	/**
	 * Prüft ob das Objekt in eine Collection mit der übergebenen Klasse überführt werden kann.
	 * @param desiredBeanClass die ZielObjekte in der Collection
	 * @param providerBean das Bean, welches in die Colleciton überführt werden soll.
	 * @return
	 */
	public static boolean canGetCollection(Class<?> desiredBeanClass, Object providerBean){
		if(providerBean==null||desiredBeanClass == null) return false;
		if(providerBean instanceof Collection){
			return canGetCollectionFromCollection(desiredBeanClass, (Collection<?>) providerBean);
		}
	
		return canGetCollectionFromSimpleObject(desiredBeanClass, providerBean);
	}

	private static boolean canGetCollectionFromSimpleObject(Class<?> desiredBeanClass, Object providerBean) {
		if(desiredBeanClass.isAssignableFrom(providerBean.getClass())){
			return true;
		}
		if(providerBean instanceof IHierarchyAdapter){
			boolean result =  ((IHierarchyAdapter)providerBean).canGetChildrenCollection(desiredBeanClass);
			if(result) return result;
		}

		if(providerBean instanceof IAdaptable){
			boolean result =  (((IAdaptable)providerBean).getAdapter(desiredBeanClass)!=null);
			if(result) return result;
		}
		Object adapted = Platform.getAdapterManager().getAdapter(providerBean, desiredBeanClass);
		if(adapted!=null){
			return true;
		}
		return false;
	}

	private static boolean canGetCollectionFromCollection(
			Class<?> desiredBeanClass, Collection<?> providerBean) {
		for(Object o:providerBean){
			if(!canGetCollection(desiredBeanClass, o)){
				return false;
			}
		}
		return true;
	}

	/**
	 * Überführt das Bean in eine Collection unter Einbzug von Delegates wie IAdaptable und IHierarchyAdapter
	 * @param desiredBeanClass
	 * @param providerBean
	 * @return
	 */
	public static Collection<?> getCollection(Class<?> desiredBeanClass, Object providerBean){
		if(providerBean==null) return null;
		if(providerBean instanceof Collection){
			return getCollectionFromCollection(desiredBeanClass, (Collection<?>) providerBean);
		}
	
		return getCollectionFromSimpleObject(desiredBeanClass, providerBean);
	}
	
	
	private static Collection<?> getCollectionFromSimpleObject(
			Class<?> desiredBeanClass, Object providerBean) {
		return testSingleEntry(desiredBeanClass, providerBean);
	}


	private static Collection<?> getCollectionFromCollection(Class<?> desiredBeanClass, Collection<?> providerBeans){
		if(providerBeans==null || providerBeans.isEmpty()){return providerBeans;}
		if(providerBeans.size()==1){
			return testSingleEntry(desiredBeanClass, providerBeans.iterator().next());
		}
		return testListEntries(desiredBeanClass,providerBeans);
	}

	private static Collection<?> testListEntries(Class<?> desiredBean,
			Collection<?> selectedBeans) {
		for(Object entry:selectedBeans){
			if(!desiredBean.isAssignableFrom(entry.getClass())){
				return null;
			}
		}
		return selectedBeans;
	}

	@SuppressWarnings("rawtypes")
	private static Collection<?> testSingleEntry(Class<?> desiredBean,
			Object object) {
		if(desiredBean==null||object==null){
			return new ArrayList();
		}
		if(desiredBean.isAssignableFrom(object.getClass())){
			return returnAsList(object);
		}
		if(object instanceof IHierarchyAdapter){
			if(((IHierarchyAdapter)object).canGetChildrenCollection(desiredBean)){
				return testHierarchyAdapter(desiredBean, (IHierarchyAdapter)object);
			}
			
		}


		if(object instanceof IAdaptable){
			if(((IAdaptable)object).getAdapter(desiredBean)!=null){
				return testAdaptable( desiredBean, (IAdaptable)object);
			}
		}
		Object adapted = Platform.getAdapterManager().getAdapter(object, desiredBean);
		if(adapted!=null){
			return returnAsList(adapted);
		}
		

		return null;
	}
	
	private static Collection<?> testHierarchyAdapter(Class<?> desiredBean,
			IHierarchyAdapter object) {
		if(object.canGetChildrenCollection(desiredBean)){
			return object.getChildrenCollection(desiredBean);
		}
		return null;
	}

	private static Collection<?> testAdaptable(Class<?> desiredBeanClass,
			IAdaptable object) {
			Object adapted = object.getAdapter(desiredBeanClass);
			if(adapted!=null){
				returnAsList(adapted);
			}
		return null;
	}

	private static List<Object> returnAsList(Object o){
		List<Object> alist = new ArrayList<Object>();
		alist.add(o);
		return alist;
	}
}
