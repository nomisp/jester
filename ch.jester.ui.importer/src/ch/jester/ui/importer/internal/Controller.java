package ch.jester.ui.importer.internal;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.wizard.IWizardContainer;
import org.eclipse.jface.wizard.WizardPage;

import ch.jester.common.ui.utility.UIUtility;
import ch.jester.commonservices.api.importer.IImportHandlerEntry;
import ch.jester.commonservices.api.importer.IImportManager;
import ch.jester.commonservices.api.importer.IWebImportAdapter;
import ch.jester.commonservices.api.importer.IWebImportHandlerEntry;
import ch.jester.commonservices.util.ServiceUtility;

public class Controller {
	private StructuredViewer mProvV, mDLV;
	private CheckboxTableViewer mFEV; 
	private ComboViewer mHV;
	private static Controller instance;
	private StructuredViewer[] mViewerHierarchy = new StructuredViewer[4];
	private WizardPage mPage;
	private ServiceUtility mService = new ServiceUtility();
	private List<IWebImportHandlerEntry> webentries;
	private List<IImportHandlerEntry> entries;
	private int mode = 2;
	private ImportData model = new ImportData();
	private boolean oldIsPageFinished;
	private Controller(WizardPage pPage){
		mPage = pPage;
		IImportManager manager = mService.getService(IImportManager.class);
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
		//System.out.println(finalized);
		UIUtility.syncExecInUIThread(new Runnable() {
			@Override
			public void run() {
				mPage.setPageComplete(finalized);
				mPage.getWizard().getContainer().updateButtons();
			}
		});
		
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
			//System.out.println(shorttype);
		}
		return tmp.toArray();
	}
	public static Controller createController(WizardPage pPage){
		instance =  new Controller(pPage);
		return instance;
	}
	public static Controller getController(){
		return instance;
	}

	
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
	void setWebAdapter(IWebImportAdapter entry){
		try {
			setHierarchyChangingInput(mDLV, entry.getLinks());
			//mDLV.setInput(entry.getLinks());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void setZipFile(String pZip){
		model.zipFile=pZip;
		model.zipEntry=null;
		model.handlerEntry = null;
		setHierarchyChangingInput(mFEV, pZip);
		
		Object[] obj = mFEV.getCheckedElements();
		/*if(obj.length==1){
			Object selected = obj[0];
			mFEV.setSelection(new StructuredSelection(selected),true);
			//System.out.println(selected);
			setSelectedZipEntry(selected.toString());
			
		}*/
		
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


	public boolean isPageComplete() {
		return oldIsPageFinished;
	}

}