package ch.jester.commonservices.impl.persistencyevent;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import ch.jester.commonservices.api.persistencyevent.IPersistencyEventQueue;
import ch.jester.commonservices.api.persistencyevent.IPersistencyListener;
import ch.jester.commonservices.api.persistencyevent.PersistencyEvent;




public class PersistencyEventQueue implements IPersistencyEventQueue {
	private static PersistencyEventQueue mQ;
	private BlockingQueue<PersistencyEvent> mQueue = new ArrayBlockingQueue<PersistencyEvent>(100);
	private PersistencyEventDaemonJob mSenderJob;
	private PersistencyEventQueue(){}
	public static synchronized PersistencyEventQueue getDefault(){
		if(mQ==null){
			mQ = new PersistencyEventQueue();
			mQ.mSenderJob=new PersistencyEventDaemonJob(mQ);
		}
		return mQ;
	}
	public PersistencyEventDaemonJob getSenderJob(){
		return mSenderJob;
	}
	
	@Override
	public void dispatch(PersistencyEvent pEvent){
		try {
			mQueue.put(pEvent);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Override
	public PersistencyEvent getEvent() throws InterruptedException{
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
