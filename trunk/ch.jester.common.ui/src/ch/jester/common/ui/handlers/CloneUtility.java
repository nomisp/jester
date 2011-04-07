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

public class CloneUtility {

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
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (NoSuchMethodException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
		return transferObjectList;
	}

	public static StructuredTransferSelection copyClonesForTransfer(IStructuredSelection pSelection){
		return new StructuredTransferSelection(createClones(pSelection));
	}
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
		@SuppressWarnings("rawtypes")
		@Override
		public Iterator iterator(){
			return paste().iterator();
		}
	}
}
