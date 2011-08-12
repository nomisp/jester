package ch.jester.common.ui.handlers;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchWizard;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.wizards.IWizardDescriptor;

/**
 * Versucht einen Wizard aufzurufen.
 * (WizardId = eigeneId)
 *
 */
public class WizardCallingHandler extends AbstractCommandHandler{

	@Override
	public Object executeInternal(ExecutionEvent event) {
		String wizardId = event.getCommand().getId();
		//1. new Wizards 
		IWizardDescriptor wizardDescriptor = PlatformUI.getWorkbench().getNewWizardRegistry().findWizard(wizardId);
		
		//2. Import Wizards
		if(wizardDescriptor==null){
			 wizardDescriptor = PlatformUI.getWorkbench().getImportWizardRegistry().findWizard(wizardId);
		}
		
		//3. Export Wizard
		if(wizardDescriptor==null){
			 wizardDescriptor = PlatformUI.getWorkbench().getExportWizardRegistry().findWizard(wizardId);
		}
	
			IWorkbenchWizard wizard = null;;
			try {
				wizard = wizardDescriptor.createWizard();
			} catch (CoreException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			wizard.init(PlatformUI.getWorkbench(), StructuredSelection.EMPTY);
			
			if (wizardDescriptor.canFinishEarly() && !wizardDescriptor.hasPages()) {
				wizard.performFinish();
				return null;
			}
			
			Shell parent = 	 HandlerUtil.getActiveWorkbenchWindow(event).getShell();
			  
			WizardDialog dialog = new WizardDialog(parent, wizard){
				@Override
				public void updateButtons() {
					if(getCurrentPage()==null){return;}
					super.updateButtons();
				}
			};
			dialog.open();

		
	        
	        
	        return null;
	}
}
