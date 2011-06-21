package ch.jester.commonservices.impl.web;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.UnknownHostException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.core.runtime.preferences.IPreferencesService;
import org.eclipse.jface.action.StatusLineManager;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.osgi.service.component.ComponentContext;

import ch.jester.common.components.InjectedLogFactoryComponentAdapter;
import ch.jester.common.ui.labelprovider.ImageStatusLineContributionItem;
import ch.jester.common.ui.services.IExtendedStatusLineManager;
import ch.jester.common.ui.utility.UIUtility;
import ch.jester.common.web.HTTPFactory;
import ch.jester.commonservices.api.preferences.IPreferenceManager;
import ch.jester.commonservices.api.preferences.IPreferenceManagerProvider;
import ch.jester.commonservices.api.preferences.IPreferenceProperty;
import ch.jester.commonservices.api.preferences.IPreferencePropertyChanged;
import ch.jester.commonservices.api.preferences.IPreferenceRegistration;
import ch.jester.commonservices.api.web.IPingService;
import ch.jester.commonservices.impl.internal.Activator;
import ch.jester.commonservices.util.ServiceUtility;

public class JavaPingService extends InjectedLogFactoryComponentAdapter<IPreferenceRegistration>implements IPingService, IPreferenceManagerProvider{
	private PingJob job;
	private Image mOk, mNok, mU;
	private ServiceUtility mService = ServiceUtility.getUtility();
	private IPreferenceManager mPrefManager;
	public JavaPingService(){

	}
	
	@Override
	public void bind(IPreferenceRegistration pT) {
		mPrefManager = pT.createManager();
		mPrefManager.setId("ch.jester.pingservice");
		mPrefManager.create("address", "Ping Address", "http://www.google.com");
		mPrefManager.create("interval", "Ping Interval (ms)", 5*1000);

		mPrefManager.addListener(new IPreferencePropertyChanged() {
			@Override
			public void propertyValueChanged(String internalKey, Object mNewValue,
					IPreferenceProperty preferenceProperty) {
				
			}
		});
		mPrefManager.registerProviderAtRegistrationService(this);
		
		String adr = mPrefManager.getPropertyByInternalKey("address").getValue().toString();
		int intr = (Integer) mPrefManager.getPropertyByInternalKey("interval").getValue();
		this.ping(adr, intr);
		getLogger().debug("Ping Component Config: pinging "+adr+" every "+intr+" ms");
	}
	@Override
	public int ping(String pInetAddress) {
		try {
			HttpURLConnection hc = HTTPFactory.connect(pInetAddress, false);
			hc.connect();
			hc.disconnect();
			return REACHABLE;
	
		} catch (UnknownHostException e) {
			//e.printStackTrace();
		} catch (IOException e) {
			//e.printStackTrace();
		}
		return NOT_REACHABLE;
	}

	@Override
	public int ping(String pInetAddress, int pReschedule) {
		if(job==null){
			job = new PingJob();
			job.schedule(5000);
		}
		return 0;
	}

	private Image getImage(String pPath){
		return UIUtility.getImageDescriptor(
				Activator.getDefault().getActivationContext().getPluginId(),
				pPath).createImage();
	}

	
	class PingJob extends Job{
		String mInetAddress; 
		int mReschedule;
		boolean uiInstalled = false;
		int lastResult = -1;
		final ImageStatusLineContributionItem sl;
		public PingJob() {
			super("PingJob");
			setSystem(true);
			sl=new ImageStatusLineContributionItem("IStatus");
			
		}
		
		private void updateVars(){
			mInetAddress =  mPrefManager.getPropertyByInternalKey("address").getValue().toString();
			mReschedule = (Integer)mPrefManager.getPropertyByInternalKey("interval").getValue();
			
		}

		@Override
		protected IStatus run(IProgressMonitor monitor) {
			updateVars();
			if(uiInstalled){
				final int result = JavaPingService.this.ping(mInetAddress);
				if(result!=lastResult){
					UIUtility.syncExecInUIThread(new SettingRunnable(result));
				}
				lastResult = result;
				schedule(mReschedule);
			}else{				
				try{
					boolean uiready = UIUtility.isUIReady();
					if(uiready){
						UIUtility.syncExecInUIThread(new UnknownRunnable());
						schedule(mReschedule);
					}else{
						schedule(200);
					}

				}catch(Exception e){
					schedule(200);
				}
			}
			
			return Status.OK_STATUS;
		}
		class SettingRunnable implements Runnable {
			int result;
			public SettingRunnable(int pResult) {
				result = pResult;
			}

			@Override
			public void run() {
				if(Display.getDefault().isDisposed()){
					return;
				}
				if(result == REACHABLE){
					if(mOk==null){
					mOk= getImage("icons/connection_ok_16px.png");
					}
					sl.setImage(mOk);
					sl.setText("");
					sl.setToolTipText("Internet Connection:ok");
					IExtendedStatusLineManager ex = Activator.getDefault().getActivationContext().getService(IExtendedStatusLineManager.class);

					//ex.setMessage(img, "internet ok");
					ex.update(true);
					
				}else{
					if(mNok==null){
						mNok= getImage("icons/connection_nok_16px.png");
						}
						sl.setImage(mNok);
						sl.setText("");
						sl.setToolTipText("Internet Connection: failed");
						IExtendedStatusLineManager ex = Activator.getDefault().getActivationContext().getService(IExtendedStatusLineManager.class);

						//ex.setMessage(img, "internet ok");
						ex.update(true);
				}
				
			}
		}
		
		class UnknownRunnable implements Runnable {
			
			@Override
			public void run() {
				IExtendedStatusLineManager ex = Activator.getDefault().getActivationContext().getService(IExtendedStatusLineManager.class);
				ex.appendToGroup(StatusLineManager.END_GROUP, sl);
				sl.setParent(ex);
				sl.setText("");
				sl.setToolTipText("Internet Connection: unknown");
				if(mU==null){
					mU = getImage("icons/connection_unk_16px.png");
			
				}
				sl.setImage(mU);
				ex.update(true);
				lastResult=UNKNOWN;
				uiInstalled=true;
				
			}
		}
	}

	@Override
	public boolean isConnected() {
		if(job==null){
			return false;
		}
		return job.lastResult==REACHABLE;
	}

	@Override
	public IPreferenceManager getPreferenceManager(String pKey) {
		return mPrefManager.checkId(pKey);
	}

}
