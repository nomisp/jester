package ch.jester.adapter;

import messages.Messages;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipselabs.p2.rcpupdate.utils.P2Util;
import org.osgi.service.component.ComponentContext;

import ch.jester.common.components.InjectedLogFactoryComponentAdapter;
import ch.jester.common.ui.utility.UIUtility;
import ch.jester.commonservices.api.preferences.IPreferenceManager;
import ch.jester.commonservices.api.preferences.IPreferenceManagerProvider;
import ch.jester.commonservices.api.preferences.IPreferenceProperty;
import ch.jester.commonservices.api.preferences.IPreferenceRegistration;

public class PeriodicUpdateHandler extends InjectedLogFactoryComponentAdapter<IPreferenceRegistration>{
	private final static String PM_ID = "ch.jester.update"; //$NON-NLS-1$
	private final static String PP_ID_INTERVAL = "interval"; //$NON-NLS-1$
	private final static String PP_DEF_INTERVAL = "startup"; //$NON-NLS-1$
	private static final String[][] PP_SELECTDEF_INTERVAL= new String[][]{{Messages.PeriodicUpdateHandler_3,"startup"},{Messages.PeriodicUpdateHandler_5,"manually"}}; //$NON-NLS-2$ //$NON-NLS-4$
	private IPreferenceProperty mInterval;
	
	private IPreferenceManager mManager;
	
	public PeriodicUpdateHandler(){
		
	}
	
	void initPreferences(IPreferenceRegistration pT){
		mManager = pT.createManager();
		mManager.setId(PM_ID);
		mInterval = mManager.create(PP_ID_INTERVAL, "Update", PP_DEF_INTERVAL); //$NON-NLS-1$
		mInterval.setSelectableValues(PP_SELECTDEF_INTERVAL);
		mManager.registerProviderAtRegistrationService(PM_ID, new IPreferenceManagerProvider() {
			@Override
			public IPreferenceManager getPreferenceManager(String pKey) {
				return mManager;
			}
		});
	}
	
	@Override
	public void bind(IPreferenceRegistration pT) {
		initPreferences(pT);
		if(doRun()){
			getLogger().info("Running Update check"); //$NON-NLS-1$
			executeAsyncUpdate();
		}else{
			getLogger().info("Update check not executed"); //$NON-NLS-1$
		}
		
	}
	
	private void executeAsyncUpdate() {
		Job job = new Job(Messages.PeriodicUpdateHandler_10){
			@Override
			protected IStatus run(IProgressMonitor monitor) {
				monitor.beginTask(Messages.PeriodicUpdateHandler_10, IProgressMonitor.UNKNOWN); //$NON-NLS-1$
				try {
					Thread.sleep(4000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				while(!UIUtility.isUIReady()){
					try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				P2Util.checkForUpdates(monitor);
				monitor.done();
				return Status.OK_STATUS;
			}
			
		};
		job.schedule(2000);
		
	}

	@Override
	public void stop(ComponentContext pComponentContext) {
		getLogger().info("Stopped PeriodicUpdateHandler"); //$NON-NLS-1$
	}

	private boolean doRun() {
		return mInterval.getStringValue().equals("startup"); //$NON-NLS-1$
	}
}
