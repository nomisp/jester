package ch.jester.ui.importer.internal;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.ISafeRunnable;
import org.eclipse.core.runtime.SafeRunner;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.wizard.WizardPage;

import ch.jester.common.ui.utility.UIUtility;
import ch.jester.common.utility.ExceptionUtility;
import ch.jester.common.utility.ExceptionWrapper;
import ch.jester.commonservices.api.importer.IImportHandlerEntry;
import ch.jester.commonservices.api.importer.IImportManager;
import ch.jester.commonservices.api.importer.ILink;
import ch.jester.commonservices.api.importer.IWebImportAdapter;
import ch.jester.commonservices.api.importer.IWebImportHandlerEntry;
import ch.jester.commonservices.exceptions.ProcessingException;
import ch.jester.commonservices.util.ServiceUtility;

public class Controller {
	private StructuredViewer mProvV, mDLV;
	private CheckboxTableViewer mFEV; 
	private ComboViewer mHV;
	//private static Controller instance;
	private StructuredViewer[] mViewerHierarchy = new StructuredViewer[4];
	private WizardPage mPage;
	private ServiceUtility mService = new ServiceUtility();
	private List<IWebImportHandlerEntry> webentries;
	private List<IImportHandlerEntry> entries;
	private int mode = 2;
	private ImportData model = new ImportData();
	private boolean oldIsPageFinished;
	private boolean importPossible = true;
	private int mCurrentPage = 1;
	private ParseController parseController = new ParseController();
	public Controller(){
		//mPage = pPage;
		IImportManager manager = mService.getService(IImportManager.class);
		mService.closeAllTrackers();
		List<IImportHandlerEntry> handlers = manager.getRegistredEntries();
		webentries = new ArrayList<IWebImportHandlerEntry>();
		entries = new ArrayList<IImportHandlerEntry>();
		for(IImportHandlerEntry e:handlers){
			if(e instanceof IWebImportHandlerEntry){
				webentries.add((IWebImportHandlerEntry)e);
			}else{
				entries.add(e);
			}
		}
	}
	public void setPage(WizardPage pPage){
		mPage=pPage;
	}
	
	public ParseController getParseController(){
		return parseController;
	}
	
	
	public void setWebMode(){
		mode = 1;
		setHierarchyChangingInput(mProvV, 1);
		enableWebMode(true);
		reset();
		checkState();
	}
	
	public void setZipMode(){
		mode = 2;
		setHierarchyChangingInput(mProvV, null);
		enableZipMode(true);
		reset();
		checkState();
	}
	void enableZipMode(boolean b){
		mDLV.getControl().setEnabled(!b);
		mProvV.getControl().setEnabled(!b);
		mFEV.getControl().setEnabled(b);
		mHV.getControl().setEnabled(b);
	}
	
	void enableWebMode(boolean b){
		mDLV.getControl().setEnabled(b);
		mProvV.getControl().setEnabled(b);
		mFEV.getControl().setEnabled(b);
		mHV.getControl().setEnabled(b);
	}

	
	public void reset(){
		model.reset();
	}
	public List<? extends IImportHandlerEntry> getHandlersForCurrentMode(){
		return mode==2?entries:webentries;
	}
	private void checkState(){
		boolean pageComplete;
		if(mode == 1){
			if(model.handlerEntry!=null&&model.webEntry!=null&&model.zipFile!=null&&model.zipEntry!=null){
				//mPage.setPageComplete(true);
				pageComplete=true;
			}else{
				//mPage.setPageComplete(false);
				pageComplete=false;
			}	
		}else{
			if(model.handlerEntry!=null&&model.zipFile!=null&&model.zipEntry!=null){
				//mPage.setPageComplete(true);
				pageComplete=true;
			}else{
			//	mPage.setPageComplete(false);
				pageComplete=false;
			}
		}

		final boolean finalized = pageComplete;
		if(oldIsPageFinished==finalized){
			return;
		}
		oldIsPageFinished=finalized;
		UIUtility.syncExecInUIThread(new Runnable() {
			@Override
			public void run() {
				mPage.setPageComplete(finalized);
				mPage.getWizard().getContainer().updateButtons();
			}
		});
		if(oldIsPageFinished){
			
			doTestRun();
		}
		
	}
	private void doTestRun() {
		UIUtility.busyIndicatorJob("Test Run", new UIUtility.IBusyRunnable() {
			@Override
			public void stepOne_InUIThread() {
				setImportPossible(false);
				mPage.setPageComplete(false);
				mPage.getWizard().getContainer().updateButtons();
				mPage.setErrorMessage(null);
				
			}
			
			@Override
			public void stepTwo_InJob() {
				//ParseController.getController().testRun();
				SafeRunner.run(new ISafeRunnable() {
					
					@Override
					public void run() throws Exception {
						getParseController().testRun();
					}
					
					@Override
					public void handleException(final Throwable exception) {
						UIUtility.syncExecInUIThread(new Runnable(){

							@Override
							public void run() {
								setImportPossible(false);
								mPage.setPageComplete(false);
								if(mCurrentPage==1){
									mPage.setPageComplete(true);
								}
								mPage.getWizard().getContainer().updateButtons();
								ExceptionWrapper ew = ExceptionUtility.wrap(exception, ProcessingException.class);
								mPage.setErrorMessage(ew.getThrowableMessage());
							}
							
							
						});
		
					}
				});
				
			}
			

			
			@Override
			public void finalStep_inUIThread() {
				setImportPossible(true);
				mPage.setPageComplete(true);
				mPage.getWizard().getContainer().updateButtons();
				
			}
		});
		
		/*SafeRunner.run(new ISafeRunnable() {
			
			@Override
			public void run() throws Exception {
				setImportPossible(false);
				mPage.setPageComplete(false);
				mPage.getWizard().getContainer().updateButtons();
				mPage.setErrorMessage(null);
				ParseController.getController().testRun();
				setImportPossible(true);
				mPage.setPageComplete(true);
				mPage.getWizard().getContainer().updateButtons();
			}
			
			@Override
			public void handleException(Throwable exception) {
				Controller.getController().setImportPossible(false);
				mPage.setPageComplete(false);
				if(mCurrentPage==1){
					mPage.setPageComplete(true);
				}
				mPage.getWizard().getContainer().updateButtons();
				ExceptionWrapper ew = ExceptionUtility.wrap(exception, ProcessingException.class);
				mPage.setErrorMessage(ew.getThrowableMessage());
			}
		});*/
		
	}




	public Object[] filterZipEntries(Object[] array) {
		if(model.webEntry!=null){
			return filterWeb(array);
		}
		return array;
	}
	private Object[] filterWeb(Object[] array) {
		String shorttype = model.webEntry.getShortType();
		if(shorttype.indexOf("*.")!=-1){			
			shorttype = shorttype.substring(shorttype.indexOf("*.")+2);
		}else if(shorttype.indexOf(".")!=-1){			
			shorttype = shorttype.substring(shorttype.indexOf(".")+1);
		}
		List<String> tmp = new ArrayList<String>();
		for(int i=0;i<array.length;i++){
			if(array[i].toString().endsWith("."+shorttype)){
				tmp.add(array[i].toString());
			}
		}
		return tmp.toArray();
	}
	/*public static Controller createController(WizardPage pPage){
		instance =  new Controller(pPage);
		return instance;
	}*/

	
	public void setProviderView(StructuredViewer viewer){
		mProvV=viewer;
		mViewerHierarchy[0]=mProvV;
	}

	public void setDownloadView(StructuredViewer viewer){
		mDLV=viewer;
		mViewerHierarchy[1]=mDLV;
	}
	public void setFileEntryView(CheckboxTableViewer viewer){
		mFEV=viewer;
		mViewerHierarchy[2]=mFEV;
	}
	public void setHandlerView(ComboViewer viewer){
		mHV=viewer;
		mViewerHierarchy[3] = mHV;
	}
	

	
	void setHierarchyChangingInput(Viewer v, Object input){
		boolean doReset = false;
		for(Viewer viewer:mViewerHierarchy){
			if(!doReset){
				if(viewer==v){
					doReset = true;
					viewer.setInput(input);
				}
			}else{
				viewer.setInput(null);
			}
		}
	}

	void resetHierarchyInput(){
		setHierarchyChangingInput(mViewerHierarchy[0], null);
	}

	public void setSelectedWebHandlerEntry(IImportHandlerEntry entry) {
		model.reset();
		model.webEntry=entry;
		IWebImportAdapter adapter = (IWebImportAdapter)entry.getService();
		setWebAdapter(adapter);
		checkState();
		
	}
	void setWebAdapter(final IWebImportAdapter entry){
		UIUtility.busyIndicatorJob("Parsing Page", new UIUtility.IBusyRunnable() {
			List<ILink> links;
			@Override
			public void stepTwo_InJob() {
				
				try {
					links = entry.getLinks();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			
			@Override
			public void stepOne_InUIThread() {
				setHierarchyChangingInput(mDLV, null);
		
				
			}
			
			@Override
			public void finalStep_inUIThread() {
				mDLV.setInput(links);
				
			}
		});
		/*try {
			setHierarchyChangingInput(mDLV, entry.getLinks());
			//mDLV.setInput(entry.getLinks());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
	}
	public void setZipFile(String pZip){
		model.zipFile=pZip;
		model.zipEntry=null;
		model.handlerEntry = null;
		setHierarchyChangingInput(mFEV, pZip);
		
		checkState();	
	}
	public void setSelectedZipEntry(String string) {
		model.zipEntry=string;
		mHV.setInput(string);
		String[] oo = mHV.getCombo().getItems();
		if(oo.length==1){
			mHV.getCombo().select(0);
			mHV.setSelection(new StructuredSelection(getHandlersForCurrentMode().get(0)), true);
		}
		checkState();
		
	}
	public void setSelectedHandlerEntry(IImportHandlerEntry entry) {
		model.handlerEntry=entry;
		checkState();
	}


	public ImportData getImportData() {
		return model;
	}


	/*public boolean isPageComplete() {
		return oldIsPageFinished;
	}*/


	public boolean canFinish() {
		checkState();
			if(!oldIsPageFinished){
				return false;
			}
		
		return importPossible;
	}

	public void setImportPossible(boolean b){
		b = importPossible;
	}




	public void setCurrentPageIndex(int i) {
		mCurrentPage = i;
		
	}
	public void clear() {
		mService.closeAllTrackers();
		
	}
}
