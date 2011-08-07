package ch.jester.common.ui.handlers;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.jface.util.LocalSelectionTransfer;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.dnd.Transfer;

/**
 * Klasse mit statischen Methoden um das cloning zu vereinfachen.
 *
 */
public class CloneUtility {

	/**
	 * Erzeugt die Klone der Objekte welche in der Selektion enthalten sind.
	 * @param pSelection
	 * @return
	 */
	private static List<?> createClones(
			IStructuredSelection pSelection) {
		Iterator<?> objectIterator = null;
		if(pSelection instanceof StructuredTransferSelection){
			objectIterator = ((StructuredTransferSelection)pSelection).getCopiedObjects().iterator();
		}else{
			objectIterator=pSelection.iterator();
		}
		List<Object> transferObjectList = new ArrayList<Object>();
		while(objectIterator.hasNext()){
			Object object = objectIterator.next();
			try {
				 Method method = object.getClass().getMethod("clone", new Class[]{});
				 Object clone = method.invoke(object, new Object[]{});
				 transferObjectList.add(clone);
				} catch (SecurityException e) {

					e.printStackTrace();
				} catch (NoSuchMethodException e) {
	
					e.printStackTrace();
				} catch (IllegalArgumentException e) {

					e.printStackTrace();
				} catch (IllegalAccessException e) {

					e.printStackTrace();
				} catch (InvocationTargetException e) {

					e.printStackTrace();
				}
		}
		return transferObjectList;
	}

	public static StructuredTransferSelection copyClonesForTransfer(IStructuredSelection pSelection){
		return new StructuredTransferSelection(createClones(pSelection));
	}
	/**
	 * Eine Selection welche bei jedem paste neue Clones erzeugt
	 *
	 */
	static class StructuredTransferSelection extends StructuredSelection{
		List<?> mObjects;
		LocalSelectionTransfer[] mTransfers = null;
		public StructuredTransferSelection(List<?> pClonedObjects){
			super(pClonedObjects);
			mObjects = pClonedObjects;
			mTransfers = new LocalSelectionTransfer[mObjects.size()];
			for(int i=0;i<mObjects.size();i++){
				mTransfers[i] = LocalSelectionTransfer.getTransfer();
				mTransfers[i].setSelection(this);
			}
		}
		public List<?> getCopiedObjects(){
			return mObjects;
		}
		public Transfer[] getCopiedTransferObjects(){
			return mTransfers;
		}
		public List<?> paste(){
			return CloneUtility.createClones(this);
		}
		
		@Override
		public List<?> toList() {
			return paste();
		}
		@Override
		public Object[] toArray() {
			return paste().toArray();
		}
		@SuppressWarnings("rawtypes")
		@Override
		public Iterator iterator(){
			return paste().iterator();
		}
	}
}
