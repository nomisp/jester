package ch.jester.common.ui.editor;


import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IPartListener2;
import org.eclipse.ui.IPartService;
import org.eclipse.ui.IWorkbenchPartReference;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;

import ch.jester.common.ui.editorutilities.DirtyManager;
import ch.jester.common.ui.editorutilities.IDirtyManagerPropertyInvoker;
import ch.jester.common.ui.editorutilities.IDirtyManagerProvider;
import ch.jester.common.ui.internal.Activator;
import ch.jester.commonservices.util.ServiceUtility;


public abstract class AbstractEditor extends EditorPart implements IDirtyManagerPropertyInvoker, IDirtyManagerProvider{
	private ServiceUtility mServices = Activator.getDefault().getActivationContext().getServiceUtil();
	private DirtyManager mDirtyManager;

	@Override
	public void init(IEditorSite site, IEditorInput input)
			throws PartInitException {
		 IPartService service = (IPartService) getSite().getService(IPartService.class);
		 service.addPartListener(new IPartListener2() {
			
			@Override
			public void partVisible(IWorkbenchPartReference partRef) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void partOpened(IWorkbenchPartReference partRef) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void partInputChanged(IWorkbenchPartReference partRef) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void partHidden(IWorkbenchPartReference partRef) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void partDeactivated(IWorkbenchPartReference partRef) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void partClosed(IWorkbenchPartReference partRef) {
				if(partRef == AbstractEditor.this){
					editorClosed();
				}
				
			}
			
			@Override
			public void partBroughtToTop(IWorkbenchPartReference partRef) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void partActivated(IWorkbenchPartReference partRef) {
				// TODO Auto-generated method stub
				
			}
		});
		
	}
	
	public ServiceUtility getServiceUtil(){
		return mServices;
	}
	
	protected void setDirtyManager(DirtyManager pDirtyManager){
		mDirtyManager=pDirtyManager;
	}
	
	public DirtyManager getDirtyManager(){
		return mDirtyManager;
	}
	protected void cleanDirty(){
		mDirtyManager.reset();
	}
	
	@Override
	public boolean isDirty() {
		return mDirtyManager.isDirty();
	}
	

	@Override
	public void doSaveAs() {
		// Do the Save As operation
	}
	@Override
	public boolean isSaveAsAllowed() {
		return false;
	}
	
	@Override
	public void setFocus() {
		
	}

	@Override
	public void fireDirtyProperty() {
		firePropertyChange(IEditorPart.PROP_DIRTY);
	}
	
	
	 public void editorClosed(){};
}
