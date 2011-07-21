package ch.jester.common.job;

import java.util.Stack;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.ISchedulingRule;
import org.eclipse.core.runtime.jobs.Job;

import ch.jester.common.activator.internal.CommonActivator;
import ch.jester.common.utility.StopWatch;
import ch.jester.commonservices.api.logging.ILogger;

/**
 * Job der einen Stack von gleichen Messages abarbeitet,  wovon nur die letzte verarbeitet werden soll.
 * @param  <T >
 */
public abstract class StackJob<T> extends Job{
	private Stack<T> eventStack;
	private StopWatch watch = new StopWatch();
	private ILogger mLogger = CommonActivator.getInstance().getActivationContext().getLogger();
	public StackJob(String name, Stack<T> pEventStack) {
		super(name);
		eventStack = pEventStack;
		//Nur 1 Job soll so laufen.
		this.setRule(new MutexSchedulingRule());
	}

	@Override
	public IStatus run(IProgressMonitor monitor) {
		T lastEvent = null;
		synchronized(eventStack){
			if(eventStack.isEmpty()){
				//abbrechen wenn es nichts zu tun gibt
				System.out.println("Ending job - nothing to do");
				return Status.CANCEL_STATUS;
			}
			lastEvent = eventStack.pop();
			eventStack.clear();
		}
		try{
			if(!eventStack.isEmpty()){
				//Sofern der Stack nicht leer ist, brechen wir ab.
				//der nächste reschedule wird die Arbeit erledigen
				System.out.println("Ending Job");
				return Status.CANCEL_STATUS;
			}
		watch.start();
		IStatus status = runInternal(monitor, lastEvent);
		watch.stop();
		mLogger.info("Job duration for "+lastEvent+": "+watch.getElapsedTime());
		return status;
	}finally{
			monitor.done();
		}
	}
	
	/**
	 * Versucht den Job mit der angegebenen Verzögerung auszuführen,<br>
	 * sofern der EventStack nicht leer ist.
	 * @param pDelay
	 */
	public void reschedule(int pDelay) {
		synchronized(eventStack){
			if(!eventStack.isEmpty()){
				schedule(pDelay);
			}
		}
	}
	/**
	 * Versucht den Job sofort auszuführen.<br>
	 * Ist identisch zu reschedule(0);
	 */
	public void reschedule() {
		reschedule(0);
	}
	
	protected abstract IStatus runInternal(IProgressMonitor monitor, T event);
	
	/**
	 * Eine Exklusive Scheduling Rule
	 * <br>
	 * Jobs mit dieser Rule laufen nicht parallel
	 *
	 */
	class MutexSchedulingRule implements ISchedulingRule {
		
		@Override
		public boolean isConflicting(ISchedulingRule rule) {
			if(rule==this){return true;}
			return false;
		}
		
		@Override
		public boolean contains(ISchedulingRule rule) {
			if(rule==this){return true;}
			return false;
		}
	}
}
