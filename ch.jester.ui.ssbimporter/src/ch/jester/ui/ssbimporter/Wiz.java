package ch.jester.ui.ssbimporter;


import java.util.List;

import org.eclipse.core.runtime.Path;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.IWizardContainer;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.IImportWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.internal.dialogs.WizardCollectionElement;
import org.eclipse.ui.internal.registry.WizardsRegistryReader;
import org.eclipse.ui.model.AdaptableList;
import org.eclipse.ui.wizards.IWizardCategory;
import org.eclipse.ui.wizards.IWizardDescriptor;

import ch.jester.common.utility.ServiceUtility;
import ch.jester.commonservices.api.importer.IImportHandlerEntry;
import ch.jester.commonservices.api.importer.IImportManager;


public class Wiz extends Wizard implements IImportWizard{
	WizPage mainPage;
	ServiceUtility mService = new ServiceUtility();
	public Wiz() {
		
			
		
	}

	@Override
	public void addPages() {
		super.addPage(mainPage);
	}

	@Override
	public boolean performFinish() {
		IDialogSettings settings = getDialogSettings();
		IWizardContainer container = getContainer();
		IWizardDescriptor[] descs = PlatformUI.getWorkbench().getImportWizardRegistry().getPrimaryWizards();
		for(IWizardDescriptor desc:descs){
			String id = desc.getId();
			System.out.println(id);
		}
		IImportManager manager = mService.getService(IImportManager.class);
		List<IImportHandlerEntry> handlers = manager.getRegistredImportHandlers();
		manager.doImport(handlers.get(0), null);
		
		return true;
	}
	

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		//setWindowTitle("File Import Wizard"); //NON-NLS-1
		setNeedsProgressMonitor(true);

		IWizardCategory root = PlatformUI.getWorkbench().getImportWizardRegistry().getRootCategory();
		IWizardCategory otherCategory = (WizardCollectionElement) root
		.findCategory(new Path(
				WizardsRegistryReader.UNCATEGORIZED_WIZARD_CATEGORY));

		System.out.println(otherCategory);
	
		
	}

}
