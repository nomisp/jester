package ch.jester.commonservices.impl.persistencyevent;

import java.util.Vector;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;

import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;


import ch.jester.commonservices.api.logging.ILogger;
import ch.jester.commonservices.api.persistency.IPersistencyEvent;
import ch.jester.commonservices.api.persistency.IPersistencyEventQueue;
import ch.jester.commonservices.api.persistency.IPersistencyListener;
import ch.jester.commonservices.impl.internal.Activator;


/**
 * Background Dispatcher Job für die EventQueue.
 * Allfällig Calls werden von der Queue an diesen Job weitergereicht (z.B. shutdown, oder addListener)
 *
 */
public class PersistencyEventDaemonJob extends Job{
	private Vector<IPersistencyListener> mListeners = new Vector<IPersistencyListener>();
	private boolean run = true;
	private ILogger mLogger = Activator.getDefault().getActivationContext().getLogger();
	private IPersistencyEventQueue mQueue;
	protected PersistencyEventDaemonJob(IPersistencyEventQueue pQueue){
		super("PersistencyEventDaemonJob");
		mQueue=pQueue;
	}
	
	/**
	 * Beenden des Jobs.
	 * Wird von der Queue aus aufgerufen.
	 */
	public void shutdown() {
		run=false;
		if(getThread()!=null)getThread().interrupt();
		
	}
	
	public void addListener(IPersistencyListener listener){
		mListeners.add(listener);
	}
	


	@Override
	protected IStatus run(IProgressMonitor monitor) {
		getThread().setName(getName());
		mLogger.debug("Starting PersistencyEventDaemonJob");
		while(!monitor.isCanceled()&&run){
			IPersistencyEvent event;
			try {
				event = mQueue.getEvent();
				fireEvent(event);
				mLogger.debug("Persistency Event Queue Size: "+mQueue.size());
			} catch (InterruptedException e) {
				if(run==false){
					mLogger.debug("Stopped due to correct interrupt request");
					return Status.OK_STATUS;
				}
				e.printStackTrace();
				return new Status(Status.ERROR, "test", -1, "Job abbortion", e);
				
			}
			
			
		}
		return Status.OK_STATUS;
	}

	private void fireEvent(IPersistencyEvent event) {
		for(IPersistencyListener listener:mListeners){
			try{
				if(listener.dispatch(event)){
					listener.persistencyEvent(event);
				}
			
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}

	public void removeListener(IPersistencyListener queueListener) {
		synchronized (mListeners) {
			mListeners.remove(queueListener);
		}

		
	}

}
