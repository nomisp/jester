package ch.jester.commonservices.impl.persistencyevent;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import ch.jester.commonservices.api.persistency.IPersistencyEvent;
import ch.jester.commonservices.api.persistency.IPersistencyEventQueue;
import ch.jester.commonservices.api.persistency.IPersistencyListener;




/**
 * Defaultimplementierung der Queue.
 * Sie ist ein Singleton, mit einem separaten DispatcherJob
 *
 */
public class PersistencyEventQueue implements IPersistencyEventQueue {
	private static PersistencyEventQueue mQ;
	private BlockingQueue<IPersistencyEvent> mQueue = new ArrayBlockingQueue<IPersistencyEvent>(100);
	private PersistencyEventDaemonJob mSenderJob;
	private PersistencyEventQueue(){}
	/**Singleton
	 * @return
	 */
	public static synchronized PersistencyEventQueue getDefault(){
		if(mQ==null){
			mQ = new PersistencyEventQueue();
			mQ.mSenderJob=new PersistencyEventDaemonJob(mQ);
		}
		return mQ;
	}
	/**Gibt den Dispatcher Job zurück
	 * @return
	 */
	public PersistencyEventDaemonJob getSenderJob(){
		return mSenderJob;
	}
	
	@Override
	public void dispatch(IPersistencyEvent pEvent){
		try {
			mQueue.put(pEvent);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	@Override
	public IPersistencyEvent getEvent() throws InterruptedException{
		return mQueue.take();
	}
	@Override
	public void addListener(IPersistencyListener listener) {
		mSenderJob.addListener(listener);
		
	}
	@Override
	public void shutdown() {
		mSenderJob.shutdown();
		
	}
}
