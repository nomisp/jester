package ch.jester.rcpapplication;

import java.text.DateFormat;
import java.util.Date;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.application.IWorkbenchConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchAdvisor;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;
import org.eclipselabs.p2.rcpupdate.utils.P2Util;

import ch.jester.commonservices.api.logging.ILogger;

public class ApplicationWorkbenchAdvisor extends WorkbenchAdvisor {
	private ILogger logger = Activator.getDefault().getActivationContext().getLogger();
//	private static final String PERSPECTIVE_ID = "ch.jester.perspective.admin";
	private static final String PERSPECTIVE_ID = "ch.jester.rcpapplication.tournamentPerspective";

	public WorkbenchWindowAdvisor createWorkbenchWindowAdvisor(
			IWorkbenchWindowConfigurer configurer) {
	
		return new ApplicationWorkbenchWindowAdvisor(configurer);
	}

	public String getInitialWindowPerspectiveId() {
		return PERSPECTIVE_ID;
	}
	
	/**
	 * Zustand der Workbench speichern 
	 */
	@Override
    public void initialize(IWorkbenchConfigurer configurer) {
		super.initialize(configurer);
        configurer.setSaveAndRestore(true);
    }

    @Override
    public IStatus saveState(IMemento memento) {
        memento.createChild("jester").putString("lastOpenedDate", 
        		DateFormat.getDateTimeInstance().format(new Date()));
        return super.saveState(memento);
    }

    @Override
    public IStatus restoreState(IMemento memento) {
        if (memento != null) {
            IMemento jesterMemento = memento.getChild("jester");
            if (jesterMemento != null)
                logger.info("Last opened on: " + jesterMemento.getString("lastOpenedDate"));
        }
        return super.restoreState(memento);
    }
    
    @Override
    public void preStartup() {
    	 P2Util.checkForUpdates();
    	 //TODO Move in Prefs
    }
}
