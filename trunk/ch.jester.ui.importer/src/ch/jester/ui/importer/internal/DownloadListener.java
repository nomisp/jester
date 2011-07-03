package ch.jester.ui.importer.internal;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
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
	public DownloadListener(IWizardContainer pContainer){
		mContainer=pContainer;
	}
	
	@Override
	public void selectionChanged(SelectionChangedEvent event) {
		//final StructuredViewer mFileTableViewer = (StructuredViewer) event.getSource();
		Controller.getController().setZipFile(null);
		download(event);
		
	}

	private void download(SelectionChangedEvent event) {
		
		su.setSelection(event.getSelection());
		IFileManager fileManager = mService.getService(IFileManager.class);
		
		final ILink link = su.getFirstSelectedAs(ILink.class);
		String newFile = link.getText().replace("/", "_")+".zip";
		final File file = fileManager.createTempFile(newFile);
		try {
			
			mContainer.run(true, false, new IRunnableWithProgress() {
				
				@Override
				public void run(IProgressMonitor monitor) throws InvocationTargetException,
						InterruptedException {
					try {
						
						monitor.beginTask("Downloading: "+link.getText(), IProgressMonitor.UNKNOWN);
						if(!file.exists()){
							link.download(file.getAbsolutePath());
						}
						
						monitor.done();
					} catch (IOException e) {
						e.printStackTrace();
					}
					UIUtility.syncExecInUIThread(new Runnable(){
						public void run(){
					     List<String> list = ZipUtility.getZipEntries(file.getAbsolutePath(), false);
					        if(list!=null){
					        	Controller.getController().setZipFile(file.getAbsolutePath());
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