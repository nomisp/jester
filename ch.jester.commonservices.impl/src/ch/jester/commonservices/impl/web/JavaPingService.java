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
import org.osgi.service.component.ComponentContext;

import ch.jester.common.components.InjectedLogFactoryComponentAdapter;
import ch.jester.common.ui.labelprovider.ImageStatusLineContributionItem;
import ch.jester.common.ui.services.IExtendedStatusLineManager;
import ch.jester.common.ui.utility.UIUtility;
import ch.jester.common.web.HTTPFactory;
import ch.jester.commonservices.api.web.IPingService;
import ch.jester.commonservices.impl.internal.Activator;

public class JavaPingService extends InjectedLogFactoryComponentAdapter<Void>implements IPingService{
	private PingJob job;
	private String mPingAddress = "http://www.google.com";
	private int mPingInterval = 5 * 1000;
	private Image mOk, mNok, mU;
	public JavaPingService(){
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
			job = new PingJob(pInetAddress,  pReschedule);
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
		public PingJob(String pInetAddress, int pReschedule) {
			super("PingJob");
			setSystem(true);
			mInetAddress =  pInetAddress;
			mReschedule = pReschedule;
			sl=new ImageStatusLineContributionItem("IStatus");
		}

		@Override
		protected IStatus run(IProgressMonitor monitor) {
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
	public void start(ComponentContext pComponentContext) {
		this.ping(mPingAddress, mPingInterval);
		getLogger().debug("Ping Component Config: pinging "+mPingAddress+" every "+mPingInterval+" ms");
	}

}
