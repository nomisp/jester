package ch.jester.commonservices.impl.persistencyevent;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import ch.jester.common.persistency.util.PersistencyEvent;
import ch.jester.commonservices.api.persistency.IPersistencyEvent;
import ch.jester.commonservices.api.persistency.IPersistencyEventQueue;
import ch.jester.commonservices.api.persistency.IPersistencyListener;




public class PersistencyEventQueue implements IPersistencyEventQueue {
	private static PersistencyEventQueue mQ;
	private BlockingQueue<IPersistencyEvent> mQueue = new ArrayBlockingQueue<IPersistencyEvent>(100);
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
	public void dispatch(IPersistencyEvent pEvent){
		try {
			mQueue.put(pEvent);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
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
