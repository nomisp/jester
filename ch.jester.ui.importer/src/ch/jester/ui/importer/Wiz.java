package ch.jester.ui.importer;


import java.lang.reflect.InvocationTargetException;

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
import ch.jester.ui.importer.internal.ParseController;


public class Wiz extends Wizard implements IImportWizard, IPageChangedListener{
	private ILogger mLogger;
	private PlayerImportWizardPage firstPage;
	private PropertyChooserWizardPage secondPage;
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
			superfininsh = Controller.getController().canFinish();
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
					mLogger.debug("Zip: "+input.getSelectedZipFile());
					mLogger.debug("Handler: "+input.getSelectedHandlerEntry());	
					SafeRunner.run(new ISafeRunnable() {
						
						@Override
						public void run() throws Exception {
							secondPage.applyChanges();
							ParseController.getController().importRun(monitor);
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
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		return true;
	}



	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		setWindowTitle("Import Players");
		setNeedsProgressMonitor(true);
		firstPage = new PlayerImportWizardPage();
		secondPage = new PropertyChooserWizardPage();
		secondPage.setInput(firstPage.getData());
		
	}
	

@Override
public void pageChanged(PageChangedEvent event) {
	if(event.getSelectedPage() == secondPage){
		secondPage.setInput(firstPage.getData());
	}
	
}
	

}