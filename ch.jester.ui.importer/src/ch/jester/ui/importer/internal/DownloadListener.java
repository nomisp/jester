package ch.jester.ui.importer.internal;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import messages.Messages;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.wizard.IWizardContainer;

import ch.jester.common.ui.utility.SelectionUtility;
import ch.jester.common.ui.utility.UIUtility;
import ch.jester.common.utility.ZipUtility;
import ch.jester.commonservices.api.importer.ILink;
import ch.jester.commonservices.api.io.IFileManager;
import ch.jester.commonservices.util.ServiceUtility;

public class DownloadListener implements ISelectionChangedListener{
	SelectionUtility su = new SelectionUtility(null);
	ServiceUtility mService = new ServiceUtility();
	IWizardContainer mContainer;
	Controller mController;
	public DownloadListener(Controller mController, IWizardContainer pContainer){
		mContainer=pContainer;
		this.mController=mController;
	}
	
	@Override
	public void selectionChanged(SelectionChangedEvent event) {
		//final StructuredViewer mFileTableViewer = (StructuredViewer) event.getSource();
		mController.setZipFile(null);
		download(event);
		
	}

	private void download(SelectionChangedEvent event) {
		
		su.setSelection(event.getSelection());
		IFileManager fileManager = mService.getService(IFileManager.class);
		mService.closeAllTrackers();
		final ILink link = su.getFirstSelectedAs(ILink.class);
		if(link==null){return;}
		String newFile = link.getText().replace("/", "_")+".zip"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		final File file = fileManager.createTempFile(newFile);
		try {
			
			mContainer.run(true, false, new IRunnableWithProgress() {
				
				@Override
				public void run(IProgressMonitor monitor) throws InvocationTargetException,
						InterruptedException {
					try {
						
						monitor.beginTask(""+link.getText(), IProgressMonitor.UNKNOWN); //$NON-NLS-1$
						if(!file.exists()){
								link.download(file.getAbsolutePath());
						}
						
						monitor.done();
					} catch (IOException e) {
						if(file.exists()){
							file.delete();
						}
						UIUtility.syncExecInUIThread(new Runnable(){

							@Override
							public void run() {
								MessageDialog.openError(UIUtility.getActiveWorkbenchWindow().getShell(), Messages.DownloadListener_error_title, Messages.DownloadListener_error_cause);
								
							}
							
							
						});
						
						
						
					}
					UIUtility.syncExecInUIThread(new Runnable(){
						public void run(){
					     List<String> list = ZipUtility.getZipEntries(file.getAbsolutePath(), false);
					        if(list!=null){
					        	mController.setZipFile(file.getAbsolutePath());
					        }
						}
							
					});
			     
			   
				}
			});

		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}