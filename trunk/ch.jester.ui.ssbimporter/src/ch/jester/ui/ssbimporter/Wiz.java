package ch.jester.ui.ssbimporter;


import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.IWizardContainer;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.IImportWizard;
import org.eclipse.ui.IWorkbench;

import ch.jester.common.utility.ZipUtility;
import ch.jester.ui.ssbimporter.WizPage.ImportSelection;


public class Wiz extends Wizard implements IImportWizard{

	WizPage mainPage = new WizPage();

	@Override
	public void addPages() {
		super.addPage(mainPage);
	}

	@Override
	public boolean performFinish() {
		
		try {
			this.getContainer().run(false, false, new IRunnableWithProgress() {
				
				@Override
				public void run(IProgressMonitor monitor) throws InvocationTargetException,
						InterruptedException {
					ImportSelection input = mainPage.getImportSelection();
					System.out.println("Zip: "+input.getSelectedZipFile());
					System.out.println("Handler: "+input.getSelectedHandlerEntry());
					
					InputStream instream = ZipUtility.getZipEntry(input.getSelectedZipFile(), input.getSelectedZipEntry());
					input.getSelectedHandlerEntry().getService().handleImport(instream, monitor);
					
					
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
		//setWindowTitle("File Import Wizard"); //NON-NLS-1
		setNeedsProgressMonitor(true);
	}
	@Override
	public void setContainer(IWizardContainer wizardContainer) {
		super.setContainer(wizardContainer);
		

	}
	

}
