package ch.jester.commonservices.impl.web;

import java.io.IOException;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.action.StatusLineContributionItem;
import org.eclipse.jface.action.StatusLineManager;

import ch.jester.common.ui.services.IExtendedStatusLineManager;
import ch.jester.common.ui.utility.UIUtility;
import ch.jester.commonservices.api.web.IPingService;
import ch.jester.commonservices.impl.internal.Activator;

public class JavaPingService implements IPingService{
	Job job;
	@Override
	public int ping(String pInetAddress, int pTimeOut) {
		try {
			URL url = new URL(pInetAddress);
			HttpURLConnection hc = (HttpURLConnection) url.openConnection();
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
	public int ping(String pInetAddress, int pTimeOut, int pReschedule) {
		if(job==null){
			job = new PingJob(pInetAddress, pTimeOut, pReschedule);
			job.schedule(5000);
		}
		return 0;
	}

	class PingJob extends Job{
		String mInetAddress; 
		int mTimeOut; 
		int mReschedule;
		boolean uiInstalled = false;
		int lastResult = -1;
		final StatusLineContributionItem sl;
		public PingJob(String pInetAddress, int pTimeOut, int pReschedule) {
			super("PingJob");
			setSystem(true);
			mInetAddress =  pInetAddress;
			mTimeOut = pTimeOut;
			mReschedule = pReschedule;
			sl=new StatusLineContributionItem("IStatus");
		}

		@Override
		protected IStatus run(IProgressMonitor monitor) {
			if(uiInstalled){
				final int result = JavaPingService.this.ping(mInetAddress, mTimeOut);
				if(result!=lastResult){
					UIUtility.syncExecInUIThread(new Runnable() {
						
						@Override
						public void run() {
							if(result == REACHABLE){
								sl.setText("Internetconnection: Ok");
							}else{
								sl.setText("Internetconnection: Failed");
							}
							
						}
					});
					
				}
				lastResult = result;
				schedule(mReschedule);
			}else{
				try{
				UIUtility.syncExecInUIThread(new Runnable() {
					
					@Override
					public void run() {
						Activator.getDefault().getActivationContext().getService(IExtendedStatusLineManager.class).appendToGroup(StatusLineManager.END_GROUP, sl);
						sl.setText("Internetconnection: unknown");
						lastResult=-99;
						uiInstalled=true;
						
					}
				});
			
				schedule(mReschedule);
				}catch(Exception e){
					schedule(200);
				}
			}
			
			return Status.OK_STATUS;
		}
		
	}
}
