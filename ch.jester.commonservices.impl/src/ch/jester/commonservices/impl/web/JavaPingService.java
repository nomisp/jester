package ch.jester.commonservices.impl.web;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.UnknownHostException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.action.StatusLineContributionItem;
import org.eclipse.jface.action.StatusLineManager;
import org.eclipse.swt.widgets.Display;
import org.osgi.service.component.ComponentContext;

import ch.jester.common.ui.services.IExtendedStatusLineManager;
import ch.jester.common.ui.utility.UIUtility;
import ch.jester.common.web.HTTPFactory;
import ch.jester.commonservices.api.components.IComponentService;
import ch.jester.commonservices.api.logging.ILogger;
import ch.jester.commonservices.api.logging.ILoggerFactory;
import ch.jester.commonservices.api.web.IPingService;
import ch.jester.commonservices.impl.internal.Activator;

public class JavaPingService implements IPingService, IComponentService<ILoggerFactory>{
	private PingJob job;
	private ILogger mLogger;
	private String mPingAddress = "http://www.google.com";
	private int mPingInterval = 5 * 1000;
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

	class PingJob extends Job{
		String mInetAddress; 
		int mReschedule;
		boolean uiInstalled = false;
		int lastResult = -1;
		final StatusLineContributionItem sl;
		public PingJob(String pInetAddress, int pReschedule) {
			super("PingJob");
			setSystem(true);
			mInetAddress =  pInetAddress;
			mReschedule = pReschedule;
			sl=new StatusLineContributionItem("IStatus");
		}

		@Override
		protected IStatus run(IProgressMonitor monitor) {
			if(uiInstalled){
				final int result = JavaPingService.this.ping(mInetAddress);
				if(result!=lastResult){
					UIUtility.syncExecInUIThread(new Runnable() {
						
						@Override
						public void run() {
							if(Display.getDefault().isDisposed()){
								return;
							}
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
						lastResult=UNKNOWN;
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
		mLogger.debug("Ping Component Config: pinging "+mPingAddress+" every "+mPingInterval+" ms");
	}

	@Override
	public void stop(ComponentContext pComponentContext) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void bind(ILoggerFactory pT) {
		mLogger = pT.getLogger(this.getClass());
		mLogger.debug("Ping Component started");
		
	}

	@Override
	public void unbind(ILoggerFactory pT) {
		// TODO Auto-generated method stub
		
	}
}