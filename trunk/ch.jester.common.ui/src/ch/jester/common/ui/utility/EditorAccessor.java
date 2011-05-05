package ch.jester.common.ui.utility;

import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;

import ch.jester.common.ui.services.IEditorService;

public class EditorAccessor {
	private IWorkbenchPage mPage;
	private IEditorService mEditorService;
	public EditorAccessor(IWorkbenchPage pPage, IEditorService editorService){
		mPage = pPage;
		mEditorService = editorService;
	}
	
	
	public EditorAccess lookup(Object pEditorInputObject) throws PartInitException{
		IEditorReference[] refs = mPage.getEditorReferences();
		IEditorReference pref = null;
		IEditorInputAccess<?> editorInput = null;
		for(IEditorReference ref:refs){
			IEditorInput input = ref.getEditorInput();
			if(!(input instanceof IEditorInputAccess)){
				continue;
			}
			IEditorInputAccess<?> pInput = (IEditorInputAccess<?>) input;
			if(pInput.getInput().equals(pEditorInputObject)){
				pref = ref;
				editorInput = pInput;
				return new EditorAccess(pref, pEditorInputObject, editorInput);
			}
		}
		return new EditorAccess(null, pEditorInputObject, null);
	}
	
	public class EditorAccess{
		private IEditorReference mRef;
		private IEditorPart mPart;
		private Object mOrigInput;
		private IEditorInputAccess<?> mInputAccess;
		private boolean isReused;
		public EditorAccess(IEditorReference pReference, Object origInput, IEditorInputAccess<?> pInputAccess){
			mRef = pReference;
			mOrigInput = origInput;
			mInputAccess=pInputAccess; 
			
		}
		public IEditorInputAccess<?> getEditorInputAccess(){
			return mInputAccess;
		}
		
		public boolean isAlreadyOpen(){
			return mRef!=null;
		}
		
		public IEditorPart getPart(){
			return mPart;
		}
		public IEditorReference getReference(){
			return mRef;
		}
		
		public boolean wasReactivated(){
			return isReused;
		}
		
		
		public void activate(){
			isReused = true;
			mRef.getPage().activate(mRef.getPart(true));
		}
		public void openEditor(IEditorInputAccess<?> pInput, String pEditorId) throws PartInitException{
			mPart = mPage.openEditor(pInput, pEditorId);
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
					mInputAccess = ain;
					break;
				}
			}
			
		}

		public void close() {
			if(mRef==null){return;}
			mRef.getPage().closeEditors(new IEditorReference[]{mRef}, false);
			mEditorService.closeEditor(null);
			
		}
	}
}
