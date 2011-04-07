package ch.jester.rcpapplication;

import org.eclipse.jface.action.IStatusLineManager;
import org.eclipse.swt.graphics.Point;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;

import ch.jester.common.ui.services.IExtendedStatusLineManager;
import ch.jester.common.ui.utility.DefaultExtendedStatusLineManager;


public class ApplicationWorkbenchWindowAdvisor extends WorkbenchWindowAdvisor {

	public ApplicationWorkbenchWindowAdvisor(IWorkbenchWindowConfigurer configurer) {
		super(configurer);
	}

	public ActionBarAdvisor createActionBarAdvisor(
			IActionBarConfigurer configurer) {
		return new ApplicationActionBarAdvisor(configurer);
	}

	public void preWindowOpen() {
		IWorkbenchWindowConfigurer configurer = getWindowConfigurer();
			configurer.setInitialSize(new Point(1024, 800));
			configurer.setShowPerspectiveBar(true);
			
			configurer.setShowMenuBar(true);
        	configurer.setShowCoolBar(true);
        	configurer.setShowProgressIndicator(true);
        	configurer.setShowStatusLine(true);
        	
        //	configurer.setShowFastViewBars(true);
	}
	
	@Override
	public void postWindowOpen() {
		super.postWindowOpen();
		IStatusLineManager manager = getWindowConfigurer().getActionBarConfigurer().getStatusLineManager();
		IExtendedStatusLineManager exmanager = new DefaultExtendedStatusLineManager(manager);
		Activator.getDefault().getActivationContext().getServiceUtil().registerService(IExtendedStatusLineManager.class, exmanager);
		Activator.getDefault().getActivationContext().getServiceUtil().registerService(IStatusLineManager.class, exmanager);
	}
}
