
package ch.jester.commonservices.api.components;


import org.osgi.service.component.ComponentContext;

/**
 * Default start/stop und bind/unbind Methoden für Declarative Components
 *
 *
 * @param <T> Typ für bind() und unbind()
 */
public interface IComponentService<T> {

	/**Wird vom DS-Framework aufgerufen, nach der Instanziierung<br>
	 * (sofern in der Component-Definition deklariert)
	 * @param pComponentContext der Kontext
	 */
	public abstract void start(ComponentContext pComponentContext);

	/**Wird vom DS-Framework beim Beenden aufgerufen<br>
	 * (sofern in der Component-Definition deklariert)
	 * @param pComponentContext der Kontext
	 */
	public abstract void stop(ComponentContext pComponentContext);

	/**Wird vom DS-Framework beim Hinzufügen eines Services aufgerufen<br>
	 * (sofern in der Component-Definition deklariert)
	 * @param pT Objekt vom Type T
	 */
	public abstract void bind(T pT);

	/**Wird vom DS-Framework beim Entnehmen eines Services aufgerufen<br>
	 * (sofern in der Component-Definition deklariert)
	 * @param pT Objekt vom Type T
	 */
	public abstract void unbind(T pT);
	

}