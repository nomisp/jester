package ch.jester.common.ui.editor;

import org.eclipse.ui.IEditorInput;

/**
 * Interface das Zugriff auf das aktuelle Input Objekt gibt
 *
 * @param <T>
 */
public interface IEditorInputAccess<T> extends IEditorInput{
	/**Den Input
	 * @return den Input
	 */
	public T getInput();
	/**
	 * Setzt das Input Objekt
	 * @param pT das Input Objekt
	 */
	public void setInput(T pT);
}
