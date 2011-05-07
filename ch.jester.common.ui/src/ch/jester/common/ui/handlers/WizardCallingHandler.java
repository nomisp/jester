package ch.jester.common.ui.handlers;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.wizard.IWizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWizard;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.internal.IWorkbenchHelpContextIds;
import org.eclipse.ui.internal.WorkbenchPlugin;
import org.eclipse.ui.wizards.IWizardDescriptor;
import org.eclipse.ui.wizards.IWizardRegistry;

public class WizardCallingHandler extends AbstractCommandHandler{

	@Override
	public Object executeInternal(ExecutionEvent event) {
		String wizardId = event.getCommand().getId();
		 IWizardDescriptor wizardDescriptor = PlatformUI.getWorkbench()
		   .getNewWizardRegistry().findWizard(wizardId);
	        IDialogSettings workbenchSettings = WorkbenchPlugin.getDefault()
            .getDialogSettings();
	
	        
	    	IWorkbenchWindow activeWindow;
			try {
				activeWindow = HandlerUtil
				.getActiveWorkbenchWindowChecked(event);
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

	
			IWorkbenchWizard wizard = null;;
			try {
				wizard = wizardDescriptor.createWizard();
			} catch (CoreException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			wizard.init(PlatformUI.getWorkbench(), null);
			
			if (wizardDescriptor.canFinishEarly() && !wizardDescriptor.hasPages()) {
				wizard.performFinish();
				return null;
			}
			
			Shell parent = 	 HandlerUtil.getActiveWorkbenchWindow(event).getShell();
			  
			WizardDialog dialog = new WizardDialog(parent, wizard);
			dialog.create();
			dialog.open();

		
	        
	        
	        return null;
	}
}
