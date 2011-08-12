package ch.jester.commonservices.api.adaptable;

import java.util.Collection;

/**
 * Interface um dynamisch Kindobjekte zu bekommen.
 *
 */
public interface IHierarchyAdapter {
	/**
	 * Liefert die Kindobjekte als Collection oder null<br>
	 * Wird eine Collection von der eigenen Klasse verlangt, soll diese erstellt werden!
	 * @param <T>
	 * @param clz
	 * @return
	 */
	public <T> Collection<T> getChildrenCollection(Class<T> clz);
	
	/**
	 * Ist identisch zu {@link IHierarchyAdapter#getChildrenCollection(Class)} zu implementieren,
	 * ohne jedoch eine Collection erstellen zu müssen. (Performance)
	 * @param <T>
	 * @param clz
	 * @return
	 */
	public <T> boolean canGetChildrenCollection(Class<T> clz);
}
