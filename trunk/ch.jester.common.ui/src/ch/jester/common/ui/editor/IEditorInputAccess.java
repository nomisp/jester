package ch.jester.common.ui.editor;

import org.eclipse.ui.IEditorInput;

public interface IEditorInputAccess<T> extends IEditorInput{
	public T getInput();
	public void setInput(T pT);
}
