package ch.jester.dao;

import java.util.Vector;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;

public class PersistencyEventSenderJob extends Job{
	private Vector<IPersistencyListener> mListeners = new Vector<IPersistencyListener>();
	private static PersistencyEventSenderJob mInstance = new PersistencyEventSenderJob();
	private PersistencyEventSenderJob(){
		super("PersistencySenderJob");
	}
	
	public static PersistencyEventSenderJob getInstance(){
		return mInstance;
	}
	
	
	public void addListener(IPersistencyListener listener){
		mListeners.add(listener);
	}
	


	@Override
	protected IStatus run(IProgressMonitor monitor) {
		Thread.currentThread().setName(this.getName());
		while(!monitor.isCanceled()){
			PersistencyEvent event = PersistencyEventQueue.getInstance().getEvent();
			fireEvent(event);
			
		}
		return Status.OK_STATUS;
	}

	private void fireEvent(PersistencyEvent event) {
		for(IPersistencyListener listener:mListeners){
			listener.persistencyEvent(event);
		}
	}

}
