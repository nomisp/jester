package ch.jester.common.ui.editor;


import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IPartListener2;
import org.eclipse.ui.IPartService;
import org.eclipse.ui.IWorkbenchPartReference;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.forms.editor.FormEditor;

import ch.jester.common.ui.editorutilities.DirtyManager;
import ch.jester.common.ui.editorutilities.IDirtyListener;
import ch.jester.common.ui.editorutilities.IDirtyManagerProvider;
import ch.jester.common.ui.internal.Activator;
import ch.jester.common.ui.utility.PartListener2Adapter;
import ch.jester.commonservices.api.logging.ILogger;
import ch.jester.commonservices.api.persistency.IDaoService;
import ch.jester.commonservices.api.persistency.IEntityObject;
import ch.jester.commonservices.util.ServiceUtility;


public abstract class AbstractEditor<T extends IEntityObject> extends FormEditor implements IDirtyListener, IDirtyManagerProvider{
	private ServiceUtility mServices = Activator.getDefault().getActivationContext().getServiceUtil();
	protected IEditorDaoInputAccess<T> mDaoInput;
	private DirtyManager mDirtyManager;
	private boolean mSaveIndicatorFlag;
	private IPartListener2 mPart2Listener = new NestedPart2Listener();
	protected ILogger mLogger = Activator.getDefault().getActivationContext().getLogger();
	protected IDaoService<T> mDao;
	private boolean isMultiPage;
	/**Falls false, werden keine Tabs angezeigt
	 * @param pMultiPage
	 */
	public AbstractEditor(boolean pMultiPage){
		isMultiPage = pMultiPage;
	}
	
	@Override
	public void init(IEditorSite site, IEditorInput input)
			throws PartInitException {
		
		mDaoInput = (IEditorDaoInputAccess<T>) input;
		setSite(site);
		setInput(input);
		
		getPartService().addPartListener(mPart2Listener);
		internalInit(site, mDaoInput);
	}
	public void internalInit(IEditorSite site, IEditorDaoInputAccess<T> input){
		
	}

	public void setDaoService(IDaoService<T> dao){
		mLogger.debug(this+" setting dao to "+dao);
		mDao=dao;
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
		if(mDirtyManager==null){return false;}
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
		super.setFocus();
	}

	@Override
	public void propertyIsDirty() {
		firePropertyChange(IEditorPart.PROP_DIRTY);
	}
	
	public boolean wasSaved(){
		return mSaveIndicatorFlag;
	}
	protected void setSaved(boolean pSaved){
		mSaveIndicatorFlag=pSaved;
	}
	
	protected void createPages() {
		super.createPages();
		if(isMultiPage){return;}
	    if (getPageCount() == 1 &&
			getContainer() instanceof CTabFolder) {
	            ((CTabFolder) getContainer()).setTabHeight(0);
	     }
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
