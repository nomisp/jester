package ch.jester.common.utility.persistency;

import java.util.Vector;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;

import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;


public class PersistencyEventSenderJob extends Job{
	private Vector<IPersistencyListener> mListeners = new Vector<IPersistencyListener>();
	private static PersistencyEventSenderJob mInstance = new PersistencyEventSenderJob();
	private boolean run = true;
	private PersistencyEventSenderJob(){
		super("PersistencySenderJob");
	}
	
	public void shutdown() {
		run=false;
		getThread().interrupt();
		
	}

	public static PersistencyEventSenderJob getInstance(){
		return mInstance;
	}
	
	
	public void addListener(IPersistencyListener listener){
		mListeners.add(listener);
	}
	


	@Override
	protected IStatus run(IProgressMonitor monitor) {
		getThread().setName(getName());
		while(!monitor.isCanceled()&&run){
			PersistencyEvent event;
			try {
				event = PersistencyEventQueue.getInstance().getEvent();
				fireEvent(event);
			} catch (InterruptedException e) {
				if(run==false){
					System.out.println("Shutting Down due to correct interrupt request");
					return Status.OK_STATUS;
				}
				e.printStackTrace();
				return new Status(Status.ERROR, "test", -1, "Job abbortion", e);
				
			}
			
			
		}
		return Status.OK_STATUS;
	}

	private void fireEvent(PersistencyEvent event) {
		for(IPersistencyListener listener:mListeners){
			listener.persistencyEvent(event);
		}
	}

}
