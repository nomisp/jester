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
import ch.jester.common.ui.editorutilities.IDirtyListener;
import ch.jester.common.ui.editorutilities.IDirtyManagerProvider;
import ch.jester.common.ui.internal.Activator;
import ch.jester.common.ui.utility.PartListener2Adapter;
import ch.jester.commonservices.util.ServiceUtility;


public abstract class AbstractEditor extends EditorPart implements IDirtyListener, IDirtyManagerProvider{
	private ServiceUtility mServices = Activator.getDefault().getActivationContext().getServiceUtil();
	private DirtyManager mDirtyManager;
	private IPartListener2 mPart2Listener = new NestedPart2Listener();
	@Override
	public void init(IEditorSite site, IEditorInput input)
			throws PartInitException {
		 getPartService().addPartListener(mPart2Listener);
	}
	protected IPartService getPartService(){
		return (IPartService) getSite().getService(IPartService.class);
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
	public void propertyIsDirty() {
		firePropertyChange(IEditorPart.PROP_DIRTY);
	}
	
	
	 public void editorClosed(){};
	 
	 class NestedPart2Listener extends PartListener2Adapter{
			@Override
			public void partClosed(IWorkbenchPartReference partRef) {
				if(partRef == AbstractEditor.this){
					editorClosed();
					getPartService().removePartListener(this);
				}
				
			}
	 }
}
