package ch.jester.common.ui.editor;


import org.eclipse.ui.part.EditorPart;

import ch.jester.common.ui.internal.Activator;
import ch.jester.common.utility.ServiceUtility;
import ch.jester.ui.editor.utilities.DirtyManager;

public abstract class AbstractEditor extends EditorPart{
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

}
