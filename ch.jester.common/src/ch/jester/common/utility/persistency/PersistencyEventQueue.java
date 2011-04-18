package ch.jester.common.utility.persistency;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;


public class PersistencyEventQueue {
	private BlockingQueue<PersistencyEvent> mQueue = new ArrayBlockingQueue<PersistencyEvent>(100);
	private static PersistencyEventQueue mInstance = new PersistencyEventQueue();
	
	private PersistencyEventQueue(){
		PersistencyEventSenderJob sender = PersistencyEventSenderJob.getInstance();
		sender.setSystem(true);
		sender.schedule();
	}
	
	public static PersistencyEventQueue getInstance(){
		
		return mInstance;
	}
	
	public void dispatch(PersistencyEvent pEvent){
		try {
			mQueue.put(pEvent);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public PersistencyEvent getEvent() throws InterruptedException{

		return mQueue.take();

	}
}
