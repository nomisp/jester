package ch.jester.commonservices.impl.web;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.UnknownHostException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.action.StatusLineManager;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

import ch.jester.common.components.InjectedLogFactoryComponentAdapter;
import ch.jester.common.ui.contributions.ImageStatusLineContributionItem;
import ch.jester.common.ui.services.IExtendedStatusLineManager;
import ch.jester.common.ui.utility.UIUtility;
import ch.jester.common.web.HTTPFactory;
import ch.jester.commonservices.api.preferences.IPreferenceManager;
import ch.jester.commonservices.api.preferences.IPreferenceManagerProvider;
import ch.jester.commonservices.api.preferences.IPreferenceProperty;
import ch.jester.commonservices.api.preferences.IPreferencePropertyChanged;
import ch.jester.commonservices.api.preferences.IPreferenceRegistration;
import ch.jester.commonservices.api.web.IPingService;
import ch.jester.commonservices.impl.internal.CommonServicesActivator;

public class JavaPingService extends InjectedLogFactoryComponentAdapter<IPreferenceRegistration>implements IPingService, IPreferenceManagerProvider{
	//Preference Stuff
	private static String PP_ID_ADDRESS = "address";
	private static String PP_DEF_ADDRESS = "http://www.google.com";
	private static String PP_ID_INTERVAL = "interval";
	private static int PP_DEF_INTERVAL = 5*1000;
	private static String PM_ID = "ch.jester.pingservice";
	
	//Icon Paths
	private static final String ICONS_CONNECTION_NOK_16PX_PNG = "icons/connection_nok.png";
	private static final String ICONS_CONNECTION_OK_16PX_PNG = "icons/connection_ok.png";
	private static final String ICONS_CONNECTION_UNK_16PX_PNG = "icons/connection_unknown.png";
	
	//Tooltip Text
	private static final String INTERNET_CONNECTION_OK = "Internet Connection: ok";
	private static final String INTERNET_CONNECTION_FAILED = "Internet Connection: failed";
	private static final String INTERNET_CONNECTION_UNKNOWN = "Internet Connection: unknown";
	
	//Class Memebers
	private PingJob job;
	private Image mOk, mNok, mU;
	private IPreferenceManager mPrefManager;
	public JavaPingService(){
	}
	
	private void initPreferences(IPreferenceRegistration pT){
		mPrefManager = pT.createManager();
		mPrefManager.setId(PM_ID);
		mPrefManager.create(PP_ID_ADDRESS, "Ping Address", PP_DEF_ADDRESS);
		mPrefManager.create(PP_ID_INTERVAL, "Ping Interval (ms)", PP_DEF_INTERVAL);
		mPrefManager.addListener(new IPreferencePropertyChanged() {
			@Override
			public void propertyValueChanged(String internalKey, Object mNewValue,
					IPreferenceProperty preferenceProperty) {
				
			}
		});
		mPrefManager.registerProviderAtRegistrationService(this);
	}
	private String getAddressProperty(){
		return mPrefManager.getPropertyByInternalKey(PP_ID_ADDRESS).getStringValue();
	}
	private int getIntervalProperty(){
		return mPrefManager.getPropertyByInternalKey(PP_ID_INTERVAL).getIntegerValue();
	}
	@Override
	public IPreferenceManager getPreferenceManager(String pKey) {
		return mPrefManager.checkId(pKey);
	}
	@Override
	public void bind(IPreferenceRegistration pT) {
		initPreferences(pT);
		String adr = getAddressProperty();
		int intr = getIntervalProperty();
		ping(adr, intr);
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
				CommonServicesActivator.getDefault().getActivationContext().getPluginId(),
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
			mInetAddress =  getAddressProperty();
			mReschedule = getIntervalProperty();
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
					mOk= getImage(ICONS_CONNECTION_OK_16PX_PNG);
					}
					sl.setImage(mOk);
					sl.setText("");
					sl.setToolTipText(INTERNET_CONNECTION_OK);
					IExtendedStatusLineManager ex = CommonServicesActivator.getDefault().getActivationContext().getService(IExtendedStatusLineManager.class);

					//ex.setMessage(img, "internet ok");
					ex.update(true);
					
				}else{
					if(mNok==null){
						mNok= getImage(ICONS_CONNECTION_NOK_16PX_PNG);
						}
						sl.setImage(mNok);
						sl.setText("");
						sl.setToolTipText(INTERNET_CONNECTION_FAILED);
						IExtendedStatusLineManager ex = CommonServicesActivator.getDefault().getActivationContext().getService(IExtendedStatusLineManager.class);

						//ex.setMessage(img, "internet ok");
						ex.update(true);
				}
				
			}
		}
		
		class UnknownRunnable implements Runnable {
			
			

			

			@Override
			public void run() {
				IExtendedStatusLineManager ex = CommonServicesActivator.getDefault().getActivationContext().getService(IExtendedStatusLineManager.class);
				ex.appendToGroup(StatusLineManager.END_GROUP, sl);
				sl.setParent(ex);
				sl.setText("");
				sl.setToolTipText(INTERNET_CONNECTION_UNKNOWN);
				if(mU==null){
					mU = getImage(ICONS_CONNECTION_UNK_16PX_PNG);
			
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



}
