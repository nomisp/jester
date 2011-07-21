package ch.jester.ui.importer;


import java.lang.reflect.InvocationTargetException;

import messages.Messages;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.ISafeRunnable;
import org.eclipse.core.runtime.SafeRunner;
import org.eclipse.jface.dialogs.IPageChangeProvider;
import org.eclipse.jface.dialogs.IPageChangedListener;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.PageChangedEvent;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.IWizardContainer;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IImportWizard;
import org.eclipse.ui.IWorkbench;

import ch.jester.common.ui.utility.UIUtility;
import ch.jester.common.utility.ExceptionUtility;
import ch.jester.common.utility.ExceptionWrapper;
import ch.jester.commonservices.api.logging.ILogger;
import ch.jester.commonservices.exceptions.ProcessingException;
import ch.jester.ui.importer.internal.Activator;
import ch.jester.ui.importer.internal.Controller;
import ch.jester.ui.importer.internal.ImportData;


public class ImportPlayerWizard extends Wizard implements IImportWizard, IPageChangedListener{
	private ILogger mLogger;
	private PlayerImportWizardPage firstPage;
	private PropertyChooserWizardPage secondPage;
	private Controller controller;
	
	
	@Override
	public void createPageControls(Composite pageContainer) {
		super.setHelpAvailable(true);
		super.createPageControls(pageContainer);
	}
	
	@Override
	public void setContainer(IWizardContainer wizardContainer) {

		super.setContainer(wizardContainer);
		if(wizardContainer instanceof IPageChangeProvider){
			((IPageChangeProvider)wizardContainer).addPageChangedListener(this);
		}
		
	}
	@Override
	public void addPages() {
		super.addPages();
		addPage(firstPage);
		addPage(secondPage);
		mLogger = Activator.getInstance().getActivationContext().getLogger();
		
	}
	


	@Override
	public boolean canFinish() {
		boolean superfininsh = super.canFinish();
		if(superfininsh){
			superfininsh = controller.canFinish();
		}
		
		return superfininsh;
		
	}
	
	@Override
	public boolean performFinish() {
		
		try {
			this.getContainer().run(false, false, new IRunnableWithProgress() {
				
				@Override
				public void run(final IProgressMonitor monitor) throws InvocationTargetException,
						InterruptedException {
					final ImportData input = firstPage.getData();
					mLogger.debug("Zip: "+input.getSelectedZipFile()); //$NON-NLS-1$
					mLogger.debug("Handler: "+input.getSelectedHandlerEntry());	 //$NON-NLS-1$
					SafeRunner.run(new ISafeRunnable() {
						
						@Override
						public void run() throws Exception {
							secondPage.applyChanges();
							controller.getParseController().importRun(monitor);
						}
						
						@Override
						public void handleException(Throwable exception) {
							ExceptionWrapper ew = ExceptionUtility.wrap(exception, ProcessingException.class);
							MessageDialog.openError(UIUtility.getActiveWorkbenchWindow().getShell(), Messages.ImportPlayerWizard_lbl_import_error, ew.getThrowableMessage());
							firstPage.setErrorMessage(null);
							secondPage.setErrorMessage(null);
							
						}
					});
					controller.clear();	
					controller=null;
					
					
					
				}
			});
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		return true;
	}



	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		
		setWindowTitle(Messages.ImportPlayerWizard_lbl_titel);
		setNeedsProgressMonitor(true);
		controller = new Controller();
		firstPage = new PlayerImportWizardPage(		controller);
		controller.setPage(firstPage);
		secondPage = new PropertyChooserWizardPage(		controller, 		controller.getParseController());
		secondPage.setInput(firstPage.getData());
		
	}
	
	
	

@Override
public void pageChanged(PageChangedEvent event) {
	if(event.getSelectedPage() == secondPage){
		secondPage.setInput(firstPage.getData());
		controller.setCurrentPageIndex(2);
	}else{
		controller.setCurrentPageIndex(1);
	}
	
}
	

}
