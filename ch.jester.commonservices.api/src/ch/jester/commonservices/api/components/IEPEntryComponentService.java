package ch.jester.commonservices.api.components;

import java.util.List;

/**
 * Interface für eine ExtensionPoint Komponente
 * 
 * @param <V> der EntryTyp
 * @param <T> der HandlerTyp
 */
public interface IEPEntryComponentService<V, T> extends IComponentService<T>{
	/**
	 * Gibt alle zur Zeit registrierten Entries zurück
	 * @return
	 */
	public List<V> getRegistredEntries();
}
