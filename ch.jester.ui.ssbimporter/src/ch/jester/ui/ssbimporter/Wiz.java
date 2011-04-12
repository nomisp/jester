package ch.jester.ui.ssbimporter;


import java.io.InputStream;
import java.util.List;

import org.eclipse.core.runtime.Path;
import org.eclipse.jface.dialogs.IDialogPage;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.IWizardContainer;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IImportWizard;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.internal.dialogs.WizardCollectionElement;
import org.eclipse.ui.internal.registry.WizardsRegistryReader;
import org.eclipse.ui.model.AdaptableList;
import org.eclipse.ui.wizards.IWizardCategory;
import org.eclipse.ui.wizards.IWizardDescriptor;

import ch.jester.common.utility.ServiceUtility;
import ch.jester.common.utility.ZipUtility;
import ch.jester.commonservices.api.importer.IImportHandlerEntry;
import ch.jester.commonservices.api.importer.IImportManager;
import ch.jester.ui.ssbimporter.WizPage.ImportInput;


public class Wiz extends Wizard implements IImportWizard{

	WizPage mainPage = new WizPage();
	public Wiz() {
		
			
		
	}

	@Override
	public void addPages() {
		super.addPage(mainPage);
	}

	@Override
	public boolean performFinish() {
		System.out.println("Zips: "+mainPage.getZipFilesToImport());
		System.out.println("Handler: "+mainPage.getSelectedImportHandlerEntry());
		ImportInput input = mainPage.getInputToProcess();
		InputStream instream = ZipUtility.getZipEntry(input.zipFile, input.zipEntry);
		input.entry.getService().handleImport(instream);
		
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
