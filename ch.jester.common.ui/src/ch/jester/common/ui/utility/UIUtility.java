package ch.jester.common.ui.utility;


import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.services.IEvaluationService;
import org.osgi.framework.FrameworkUtil;

import ch.jester.common.ui.internal.Activator;
import ch.jester.common.utility.CallerUtility;
import ch.jester.common.utility.CallerUtility.Caller;


public class UIUtility {
	static UIStrategy[] mStrategies = new UIStrategy[2];
	static {
		UIThreadStrategy mUIStrategy = new UIThreadStrategy();
		mStrategies[0] = mUIStrategy;
		mStrategies[1] = new NonUIThreadStrategy(mUIStrategy);
	}
	interface UIStrategy{
		public IWorkbenchWindow getWorkbenchWindow();
		public void syncExecInUIThread(Runnable pRunnable);
		public void asyncExecInUIThread(Runnable pRunnable);
	}
	
	
	public static boolean isUIThread(){
		return Display.getCurrent()!=null;
	}
	private static int getStrategyIndex(){
		return isUIThread()==true?0:1;
	}
	
	public static IEvaluationService getEvalService(){
		return (IEvaluationService) getActiveWorkbenchWindow().getService(IEvaluationService.class);
	}
	
	public static void reevaluateProperty(final String pEvaluationProperty){
		syncExecInUIThread(new Runnable(){

			@Override
			public void run() {
				getEvalService().requestEvaluation(pEvaluationProperty);
				
			}
			
		});

	}
	
	public static IWorkbenchWindow getActiveWorkbenchWindow(){
		return mStrategies[getStrategyIndex()].getWorkbenchWindow();
	}
	
	public static ImageDescriptor getImageDescriptor(String pluginId, String imageFilePath){
		return Activator.imageDescriptorFromPlugin(pluginId, imageFilePath);
	}
	public static void syncExecInUIThread(Runnable r){
		 mStrategies[getStrategyIndex()].syncExecInUIThread(r);
	}
	public static void asyncExecInUIThread(Runnable r){
		 mStrategies[getStrategyIndex()].asyncExecInUIThread(r);
	}
	
	public static void busyIndicatorJob(final String pName, final IBusyRunnable runnable){
		final Display display = getActiveWorkbenchWindow().getShell().getDisplay();
		syncExecInUIThread(new Runnable(){
			@Override
			public void run() {
				BusyIndicator.showWhile(display, new BusyRunnableJobAdapter(pName, runnable));
			}
		});
	}
	
	static class UIThreadStrategy implements UIStrategy{

		@Override
		public IWorkbenchWindow getWorkbenchWindow() {
			return PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		}

		@Override
		public void syncExecInUIThread(Runnable pRunnable) {
			pRunnable.run();			
		}

		@Override
		public void asyncExecInUIThread(Runnable pRunnable) {
			Display.getDefault().asyncExec(pRunnable);
			
		}
		
	}
	
	static class NonUIThreadStrategy implements UIStrategy{
		UIThreadStrategy mUIStrategy;
		Display mDisplay = PlatformUI.getWorkbench().getDisplay();
		public NonUIThreadStrategy(UIThreadStrategy pStrategy){
			mUIStrategy=pStrategy;
		}
		@Override
		public IWorkbenchWindow getWorkbenchWindow() {
			ResultRunnable<IWorkbenchWindow> result = new ResultRunnable<IWorkbenchWindow>(){
				@Override
				public IWorkbenchWindow runResult() {
					return mUIStrategy.getWorkbenchWindow();
				}
			};
			syncExecInUIThread(result);
			return result.get();
		}
		@Override
		public void syncExecInUIThread(Runnable pRunnable) {
			mDisplay.syncExec(pRunnable);
		}
		@Override
		public void asyncExecInUIThread(Runnable pRunnable) {
			mDisplay.asyncExec(pRunnable);
		}
		
	}
	
	static abstract class ResultRunnable<T> implements Runnable{
		T mResult;
		private void set(T t){
			mResult=t;
		}
		public T get(){
			return mResult;
		}
		@Override
		public void run() {
			set(runResult());
			
		}
		public abstract T runResult();
		
	}
  public interface IBusyRunnable{
	 	public void stepOne_InUIThread();
	 	public void stepTwo_InJob();
	 	public void finalStep_inUIThread();
  }
  static class BusyRunnableJobAdapter implements  Runnable{

	 	private String mName;
	 	private IBusyRunnable mRunnable;
	 	public BusyRunnableJobAdapter(String pName, IBusyRunnable runnable){
	 		mName = pName;
	 		mRunnable=runnable;
	 	}
		final Display display = getActiveWorkbenchWindow().getShell().getDisplay();
		final Shell shell = getActiveWorkbenchWindow().getShell();
			@Override
			public void run() {
				final MutableBoolean done = new MutableBoolean(false);
				Job job = new Job(mName){
					@Override
					protected IStatus run(IProgressMonitor monitor) {
						UIUtility.syncExecInUIThread(new Runnable(){
							@Override
							public void run() {
								mRunnable.stepOne_InUIThread();	
							}
						});
						mRunnable.stepTwo_InJob();
						UIUtility.syncExecInUIThread(new Runnable() {	
							@Override
							public void run() {
								mRunnable.finalStep_inUIThread();
							}
						});
						done.set(true);
						display.wake();
						return Status.OK_STATUS;
					}
					
				};
				job.schedule();
				 while (!done.get() && !shell.isDisposed()) {
		              if (!display.readAndDispatch())
		                display.sleep();
		         }
				
			}
			
		};
	private static class MutableBoolean{
			private boolean mB;
			public MutableBoolean(boolean b) {
				mB = b;
			}
			public void set(boolean b){
				mB=b;
			}
			public boolean get(){
				return mB;
			}
		}
}
