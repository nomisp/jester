package ch.jester.adapter;

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
	private final static String PM_ID = "ch.jester.update";
	private final static String PP_ID_INTERVAL = "interval";
	private final static String PP_DEF_INTERVAL = "startup";
	private static final String[][] PP_SELECTDEF_INTERVAL= new String[][]{{"on startup","startup"},{"manually","manually"}};
	private IPreferenceProperty mInterval;
	
	private IPreferenceManager mManager;
	
	public PeriodicUpdateHandler(){
		
	}
	
	void initPreferences(IPreferenceRegistration pT){
		mManager = pT.createManager();
		mManager.setId(PM_ID);
		mInterval = mManager.create(PP_ID_INTERVAL, "Update", PP_DEF_INTERVAL);
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
			getLogger().info("Running Update check");
			executeAsyncUpdate();
		}else{
			getLogger().info("Update check not executed");
		}
		
	}
	
	private void executeAsyncUpdate() {
		Job job = new Job("Checking for Updates"){
			@Override
			protected IStatus run(IProgressMonitor monitor) {
				monitor.beginTask("Check for updates", IProgressMonitor.UNKNOWN);
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
		getLogger().info("Stopped PeriodicUpdateHandler");
	}

	private boolean doRun() {
		return mInterval.getStringValue().equals("startup");
	}
}
