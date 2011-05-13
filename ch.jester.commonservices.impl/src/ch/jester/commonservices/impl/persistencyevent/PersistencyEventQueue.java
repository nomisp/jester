package ch.jester.commonservices.impl.persistencyevent;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import org.eclipse.core.runtime.Assert;
import org.osgi.service.component.ComponentContext;

import ch.jester.commonservices.api.components.IComponentService;
import ch.jester.commonservices.api.logging.ILogger;
import ch.jester.commonservices.api.logging.ILoggerFactory;
import ch.jester.commonservices.api.persistency.IPersistencyEvent;
import ch.jester.commonservices.api.persistency.IPersistencyEventQueue;
import ch.jester.commonservices.api.persistency.IPersistencyListener;




/**
 * Defaultimplementierung der Queue.
 * Sie ist eine Komponente, mit einem separaten DispatcherJob
 *
 */
public class PersistencyEventQueue implements IPersistencyEventQueue, IComponentService<ILoggerFactory> {
	private static PersistencyEventQueue mQ;
	private BlockingQueue<IPersistencyEvent> mQueue = new ArrayBlockingQueue<IPersistencyEvent>(100);
	private PersistencyEventDaemonJob mSenderJob;
	private ILogger mLogger;
	public PersistencyEventQueue(){
		Assert.isTrue(mQ==null,"mQ already here: duplicate instance!");
		mQ = this;
		mQ.mSenderJob=new PersistencyEventDaemonJob(mQ);
		mQ.getSenderJob().setSystem(true);
		mQ.getSenderJob().schedule();
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
	@Override
	public void start(ComponentContext pComponentContext) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void stop(ComponentContext pComponentContext) {
		mLogger.debug("Shutting down EventQueue");
		mQ.shutdown();
	
		
	}
	@Override
	public void bind(ILoggerFactory pT) {
		mLogger = pT.getLogger(this.getClass());
		mLogger.debug("Component started: PersistencyEventQueue");
		
	}
	@Override
	public void unbind(ILoggerFactory pT) {
		// TODO Auto-generated method stub
		
	}
}
