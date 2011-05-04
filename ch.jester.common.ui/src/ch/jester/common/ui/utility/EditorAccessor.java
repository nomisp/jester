package ch.jester.common.ui.utility;

import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;

public class EditorAccessor {
	private IWorkbenchPage mPage;
	
	private EditorAccess mEa;
	public EditorAccessor(IWorkbenchPage pPage){
		mPage = pPage;
	}
	
	
	public EditorAccess lookup(Object pEditorInputObject) throws PartInitException{
		IEditorReference[] refs = mPage.getEditorReferences();
		IEditorReference pref = null;
		for(IEditorReference ref:refs){
			IEditorInput input = ref.getEditorInput();
			if(!(input instanceof IEditorInputAccess)){
				continue;
			}
			IEditorInputAccess<?> pInput = (IEditorInputAccess<?>) input;
			if(pInput.getInput().equals(pEditorInputObject)){
				pref = ref;
				break;
			}
		}
		mEa = new EditorAccess(pref, pEditorInputObject);
		return mEa;
	}
	
	public class EditorAccess{
		private IEditorReference mRef;
		private Object mOrigInput;
		public EditorAccess(IEditorReference pReference, Object origInput){
			mRef = pReference;
			mOrigInput = origInput;
		}
		
		public boolean isAlreadyOpen(){
			return mRef!=null;
		}
		
		public IEditorReference getReference(){
			return mRef;
		}
		
		public void activate(){
			mRef.getPage().activate(mRef.getPart(true));
		}
		public void openEditor(IEditorInputAccess<?> pInput, String pEditorId) throws PartInitException{
			IEditorPart part = mPage.openEditor(pInput, pEditorId);
			IEditorReference[] refs = mPage.getEditorReferences();
			IEditorReference pref = null;
			for(IEditorReference ref:refs){
				IEditorInput input = ref.getEditorInput();
				if(!(input instanceof IEditorInputAccess)){
					continue;
				}
				IEditorInputAccess<?> ain = (IEditorInputAccess<?>) input;
				if(pInput.getInput().equals(mOrigInput)){
					pref = ref;
					mRef=ref;
					break;
				}
			}
			
		}

		public void close() {
			if(mRef==null){return;}
			mRef.getPage().closeEditors(new IEditorReference[]{mRef}, false);
			
			
		}
	}
}
