package ch.jester.commonservices.impl.persistencyevent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;

import ch.jester.commonservices.api.persistency.IDatabaseStateService;

/**
 * Defaultimpl für einen IDatabaseService.
 * Dieser Notifiziert Clients, falls der State von INIT auf RUN wechselt, oder direkt, wenn 
 * der State bereits RUN ist.
 *
 */
public class DefaultDatabaseStateService implements IDatabaseStateService{
	private HashMap<State, List<Runnable>> mMap = new HashMap<State, List<Runnable>>();
	private State mState = State.INIT;
	private Object mLock = new Object();
	public DefaultDatabaseStateService(){
		int k=0;
		k++;
	}
	@Override
	public void executeOnStateChange(State newState, Runnable r) {
		synchronized (mLock) {
			List<Runnable> stateListeners = mMap.get(newState);
			if(stateListeners==null){
				stateListeners = new ArrayList<Runnable>();
				mMap.put(newState, stateListeners);
			}
			if(newState == mState){
				r.run();
			}else{
				stateListeners.add(r);
			}
		}
		
	}
	public void setState(State pState){
		synchronized(mLock){
			mState = pState;
			List<Runnable> stateListeners = mMap.get(pState);
			if(stateListeners==null){return;}
			Job job = new DBInformerJob(stateListeners);
			job.schedule();
		}
		
		
	}



	@Override
	public State getState() {
		return mState;
	}
	
	class DBInformerJob extends Job{
		List<Runnable> mRunnerList;
		public DBInformerJob(List<Runnable> pRunnerList) {
			super("DB Informer Job");
			Assert.isNotNull(pRunnerList);
			mRunnerList = pRunnerList;
			setSystem(true);
		}

		@Override
		protected IStatus run(IProgressMonitor monitor) {
			for(Runnable r:mRunnerList){
				r.run();
			}
			return Status.OK_STATUS;
		}
		
	};
}
