package ch.jester.ui.ssbimporter;


import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.ISafeRunnable;
import org.eclipse.core.runtime.SafeRunner;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.IImportWizard;
import org.eclipse.ui.IWorkbench;

import ch.jester.common.ui.utility.UIUtility;
import ch.jester.common.utility.ExceptionUtility;
import ch.jester.common.utility.ExceptionWrapper;
import ch.jester.common.utility.ZipUtility;
import ch.jester.commonservices.api.logging.ILogger;
import ch.jester.commonservices.exceptions.ProcessingException;
import ch.jester.ui.ssbimporter.PlayerImportWizardPage.ImportHandlerManager;
import ch.jester.ui.ssbimporter.internal.Activator;


public class Wiz extends Wizard implements IImportWizard{
	private ILogger mLogger;
	private PlayerImportWizardPage firstPage;
	private PropertyChooserWizardPage secondPage;
	@Override
	public void addPages() {
		super.addPages();
		addPage(firstPage);
		addPage(secondPage);
		mLogger = Activator.getInstance().getActivationContext().getLogger();
	}

	
	
	@Override
	public boolean performFinish() {
		
		try {
			this.getContainer().run(false, false, new IRunnableWithProgress() {
				
				@Override
				public void run(final IProgressMonitor monitor) throws InvocationTargetException,
						InterruptedException {
					final ImportHandlerManager input = firstPage.getImportHandlerManager();
					mLogger.debug("Zip: "+input.getSelectedZipFile());
					mLogger.debug("Handler: "+input.getSelectedHandlerEntry());	
					final InputStream instream = ZipUtility.getZipEntry(input.getSelectedZipFile(), input.getSelectedZipEntry());
					
					SafeRunner.run(new ISafeRunnable() {
						
						@Override
						public void run() throws Exception {
							secondPage.applyChanges();
							input.getSelectedHandlerEntry().getService().handleImport(instream, monitor);
							instream.close();
						}
						
						@Override
						public void handleException(Throwable exception) {
							ExceptionWrapper ew = ExceptionUtility.wrap(exception, ProcessingException.class);
							MessageDialog.openError(UIUtility.getActiveWorkbenchWindow().getShell(), "Import Error", ew.getThrowableMessage());
							
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
		
		return true;
	}



	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		setWindowTitle("Import Players");
		setNeedsProgressMonitor(true);
		//ISelection sel = workbench.getActiveWorkbenchWindow().getSelectionService().getSelection();
		 firstPage = new PlayerImportWizardPage();
		 
		 secondPage = new PropertyChooserWizardPage();
		 secondPage.setInput(firstPage.getImportHandlerManager());
		
	}
@Override
public IWizardPage getNextPage(IWizardPage page) {
	if(page == secondPage){
		secondPage.init();
	}
	return super.getNextPage(page);
}
	

}