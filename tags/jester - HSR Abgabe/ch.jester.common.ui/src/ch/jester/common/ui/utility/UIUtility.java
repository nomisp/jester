package ch.jester.common.ui.utility;

import java.awt.DisplayMode;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.services.IEvaluationService;

import ch.jester.common.ui.internal.CommonUIActivator;

/**
 * Hilfsklasse welche unterstützung bezüglich UI Manipulationen von Non-UI
 * Jobs/Threads beinhaltet
 * 
 */
public class UIUtility {
	static UIStrategy[] mStrategies = new UIStrategy[2];
	static boolean initDone = false;
	static Object lock = new Object();

	interface UIStrategy {
		public IWorkbenchWindow getWorkbenchWindow();

		public void syncExecInUIThread(Runnable pRunnable);

		public void asyncExecInUIThread(Runnable pRunnable);
	}

	/**
	 * Zeigt einen Confirm Dialog für den Restart an.
	 * 
	 * @param pText
	 */
	public static void openRestartConfirmation(String pText) {
		if (pText == null) {
			pText = "Restart now?";
		}
		boolean b = MessageDialog.openQuestion(Display.getDefault()
				.getActiveShell(), "Restart needed", pText);
		if (b) {
			PlatformUI.getWorkbench().restart();
		}
	}

	/**
	 * Zeigt einen Confirm Dialog für den Restart an.
	 * 
	 * @param pText
	 */
	public static void openRestartConfirmation() {
		openRestartConfirmation(null);
	}

	private static void init() {
		if (!initDone) {
			try {
				UIThreadStrategy mUIStrategy = new UIThreadStrategy();
				mStrategies[0] = mUIStrategy;
				mStrategies[1] = new NonUIThreadStrategy(mUIStrategy);
				initDone = true;
			} catch (Exception e) {
				// do nothing
			}
		}
	}

	private static void checkInit() {
		if (!initDone) {
			synchronized (lock) {
				if (!initDone) {
					init();
				}
			}
		}

	}

	/**
	 * Ist es der UI Thread?
	 * 
	 * @return true, false
	 */
	public static boolean isUIThread() {
		return Display.getCurrent() != null;
	}

	/**
	 * Basierende auf dem aktuellen Thread, wählen wir die Strategie
	 * 
	 * @return
	 */
	private static int getStrategyIndex() {
		return isUIThread() == true ? 0 : 1;
	}

	/**
	 * Liefert einen einfachen Zugriff auf den EvaluationService
	 * 
	 * @return
	 */
	public static IEvaluationService getEvalService() {
		return (IEvaluationService) getActiveWorkbenchWindow().getService(
				IEvaluationService.class);
	}

	/**
	 * Sendet einen Request für eine Reevaluation des Properties an den
	 * EvaluationService
	 * 
	 * @param pEvaluationProperty
	 */
	public static void reevaluateProperty(final String pEvaluationProperty) {
		checkInit();
		syncExecInUIThread(new Runnable() {

			@Override
			public void run() {
				getEvalService().requestEvaluation(pEvaluationProperty);

			}

		});

	}

	/**
	 * Utility Methode, welche das grösste Displayer ermittelt
	 * 
	 * @return
	 */
	public static DisplayMode getLargestDisplay() {
		GraphicsEnvironment ge = GraphicsEnvironment
				.getLocalGraphicsEnvironment();
		GraphicsDevice[] gs = ge.getScreenDevices();
		DisplayMode biggestDisplay = gs[0].getDisplayMode();
		// Get size of each screen
		for (int i = 0; i < gs.length; i++) {
			DisplayMode dm = gs[i].getDisplayMode();
			int screenWidth = dm.getWidth();
			if (screenWidth > biggestDisplay.getWidth()) {
				biggestDisplay = dm;
			}
		}
		return biggestDisplay;
	}

	/**
	 * Das Aktive Workbench Window. Kann von jedem Job/Thread aufgerufen werden.
	 * 
	 * @return
	 */
	public static IWorkbenchWindow getActiveWorkbenchWindow() {
		checkInit();
		return mStrategies[getStrategyIndex()].getWorkbenchWindow();
	}

	/**
	 * Suchen des ImageDescriptors
	 * 
	 * @param pluginId
	 * @param imageFilePath
	 * @return
	 */
	public static ImageDescriptor getImageDescriptor(String pluginId,
			String imageFilePath) {
		return CommonUIActivator.imageDescriptorFromPlugin(pluginId,
				imageFilePath);
	}

	/**
	 * Versucht herauszufinden ob die Workbench kreiert wurde.
	 * 
	 * @return
	 */
	public static boolean isUIReady() {
		try {
			PlatformUI.getWorkbench().getDisplay();
			;
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * Führt das Runnable direkt im UIThread aus.
	 * 
	 * @param r
	 */
	public static void syncExecInUIThread(Runnable r) {
		checkInit();
		mStrategies[getStrategyIndex()].syncExecInUIThread(r);
	}

	/**
	 * Führt das Runnable irgendwann im UIThread aus
	 * 
	 * @param r
	 */
	public static void asyncExecInUIThread(Runnable r) {
		checkInit();
		mStrategies[getStrategyIndex()].asyncExecInUIThread(r);
	}

	/**
	 * Job für Busy Indication im UI
	 * 
	 * @param pName
	 * @param runnable
	 */
	public static void busyIndicatorJob(final String pName,
			final IBusyRunnable runnable) {
		final Display display = getActiveWorkbenchWindow().getShell()
				.getDisplay();
		syncExecInUIThread(new Runnable() {
			@Override
			public void run() {
				BusyIndicator.showWhile(display, new BusyRunnableJobAdapter(
						pName, runnable));
			}
		});
	}

	/**
	 * Strategie für den UIThread
	 * 
	 */
	static class UIThreadStrategy implements UIStrategy {

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

	/**
	 * Strategie für einen NonUI Thread
	 * 
	 */
	static class NonUIThreadStrategy implements UIStrategy {
		UIThreadStrategy mUIStrategy;
		Display mDisplay;

		public NonUIThreadStrategy(UIThreadStrategy pStrategy) {
			mUIStrategy = pStrategy;
			mDisplay = PlatformUI.getWorkbench().getDisplay();
		}

		@Override
		public IWorkbenchWindow getWorkbenchWindow() {
			ResultRunnable<IWorkbenchWindow> result = new ResultRunnable<IWorkbenchWindow>() {
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

	/**
	 * Runnable mit Resultat
	 * 
	 * @param <T>
	 */
	static abstract class ResultRunnable<T> implements Runnable {
		T mResult;

		private void set(T t) {
			mResult = t;
		}

		public T get() {
			return mResult;
		}

		@Override
		public void run() {
			set(runResult());

		}

		public abstract T runResult();

	}

	/**
	 * Interface das benutzt werden kann, damit der Cursor als Wait dargestellt
	 * wird.
	 * 
	 */
	public interface IBusyRunnable {
		/**
		 * Erster Step in UIThread
		 */
		public void stepOne_InUIThread();

		/**
		 * Zweiter Step: erfolgt in eigenem Job
		 */
		public void stepTwo_InJob();

		/**
		 * Letzter Step: wieder im UI Thread
		 */
		public void finalStep_inUIThread();
	}

	static class BusyRunnableJobAdapter implements Runnable {

		private String mName;
		private IBusyRunnable mRunnable;

		public BusyRunnableJobAdapter(String pName, IBusyRunnable runnable) {
			mName = pName;
			mRunnable = runnable;
		}

		final Display display = getActiveWorkbenchWindow().getShell()
				.getDisplay();
		final Shell shell = getActiveWorkbenchWindow().getShell();

		@Override
		public void run() {
			final MutableBoolean done = new MutableBoolean(false);
			Job job = new Job(mName) {
				@Override
				protected IStatus run(IProgressMonitor monitor) {
					try {
						UIUtility.syncExecInUIThread(new Runnable() {

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
					} catch (RuntimeException ex) {

						done.set(true);
						display.wake();
						throw ex;
					}
					return Status.OK_STATUS;
				}

			};
			job.setUser(false);
			job.setSystem(true);
			job.schedule();
			while (!done.get() && !shell.isDisposed()) {
				if (!display.readAndDispatch())
					display.sleep();
			}

		}

	};

	private static class MutableBoolean {
		private boolean mB;

		public MutableBoolean(boolean b) {
			mB = b;
		}

		public void set(boolean b) {
			mB = b;
		}

		public boolean get() {
			return mB;
		}
	}

}
