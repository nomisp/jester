package ch.jester.common.ui.editor;


import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.part.EditorPart;

import ch.jester.common.ui.editorutilities.DirtyManager;
import ch.jester.common.ui.editorutilities.IDirtyManagerPropertyInvoker;
import ch.jester.common.ui.internal.Activator;
import ch.jester.commonservices.util.ServiceUtility;


public abstract class AbstractEditor extends EditorPart implements IDirtyManagerPropertyInvoker{
	private ServiceUtility mServices = Activator.getDefault().getActivationContext().getServiceUtil();
	private DirtyManager mDirtyManager;
	public ServiceUtility getServiceUtil(){
		return mServices;
	}
	
	protected void setDirtyManager(DirtyManager pDirtyManager){
		mDirtyManager=pDirtyManager;
	}
	
	protected DirtyManager getDirtyManager(){
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
}
