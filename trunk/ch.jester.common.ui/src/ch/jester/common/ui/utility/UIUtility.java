package ch.jester.common.ui.utility;

import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.services.IEvaluationService;

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
	
	public static void reevaluateProperty(String pEvaluationProperty){
		getEvalService().requestEvaluation(pEvaluationProperty);
	}
	
	public static IWorkbenchWindow getActiveWorkbenchWindow(){
		return mStrategies[getStrategyIndex()].getWorkbenchWindow();
	}
	public static void syncExecInUIThread(Runnable r){
		 mStrategies[getStrategyIndex()].syncExecInUIThread(r);
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



}
