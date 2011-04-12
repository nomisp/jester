package ch.jester.common.ui.utility;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;

/**
 * Hilfsklasse um ISelection zu verarbeiten
 *
 */
public class SelectionUtility {
	ISelection mSelection;
	
	public SelectionUtility(ISelection pSelection){
		setSelection(pSelection);
	}
	/**Die aktuell zugewiesene ISelection
	 * @return die ISelection
	 */
	public ISelection getSelection(){
		return  mSelection;
	}
	
	/**Setzt eine neue ISelection
	 * @param pSelection
	 */
	public void setSelection(ISelection pSelection){
		mSelection=pSelection;
	}
	
	/**Falls die gesetzte ISelection eine IStructuredSelection ist, wird das erste Element zurückgegeben.
	 * Sonst null
	 * @return erstes Element oder null
	 */
	public Object getFirstSelected(){
		if(isIStructuredSelection()){
			IStructuredSelection selection = (IStructuredSelection) getSelection();
			return selection.getFirstElement();
		}
		return null;
	}
	
	/**
	 * Überprüft die Selection
	 * @return true | false
	 */
	public boolean isIStructuredSelection(){
		return (getSelection() instanceof IStructuredSelection);
	}
	
	public IStructuredSelection getAsStructuredSelection(){
		if(isIStructuredSelection()){
			return (IStructuredSelection) mSelection;
		}
		return null;
	}
	
	/**
	 * Ist die Selection leeer ?
	 * @return
	 */
	public boolean isEmpty(){
		if(mSelection==null){
			return true;
		}
		return mSelection.isEmpty();
	}
	
	/**Gibt die Anzahl der Elemente in der IStructuredSelection zurück
	 * @return
	 */
	public int getSelectionCount(){
		if(isIStructuredSelection()){
			IStructuredSelection struct = (IStructuredSelection) mSelection;
			return struct.toArray().length;
		}else{
			return 1;
		}
	}
	
	@SuppressWarnings("unchecked")
	public <T> T getFirstSelectedAs(Class<T> clz){
		if(getFirstSelected()!=null){
			Object first = getFirstSelected();
			if(clz.isAssignableFrom(first.getClass())){
				return (T)getFirstSelected();
			}
		}
		return null;
	}
}
