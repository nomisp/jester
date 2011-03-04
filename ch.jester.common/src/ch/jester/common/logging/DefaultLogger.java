package ch.jester.common.logging;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.ILogListener;
import org.eclipse.core.runtime.ISafeRunnable;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Plugin;
import org.eclipse.core.runtime.SafeRunner;
import org.osgi.framework.Bundle;

import ch.jester.commonservices.api.ILogger;
import ch.jester.commonservices.api.ILoggerFactory;

/**
 * Eine Default ILogger Implementierung
 */

public class DefaultLogger implements ILogger {
	private ILoggerFactory mFactory;
	private Bundle mBundle;
	private ILog mLog;
	private Set<ILogListener> mLogListeners = new HashSet<ILogListener>(1);

	public DefaultLogger(Plugin pPlugIn) {
		mBundle = pPlugIn.getBundle();
		mLog = pPlugIn.getLog();

	}

	public DefaultLogger(ILog pLog, Bundle pBundle, ILoggerFactory pFactory) {
		mLog = pLog;
		mBundle = pBundle;
		mFactory = pFactory;
	}

	@Override
	public void addLogListener(ILogListener listener) {
		synchronized (mLogListeners) {
			mLogListeners.add(listener);
		}
	}

	@Override
	public Bundle getBundle() {
		return mBundle;
	}

	@Override
	public void log(final IStatus status) {

		ILogListener[] listeners;
		synchronized (mLogListeners) {
			listeners = (ILogListener[]) mLogListeners
					.toArray(new ILogListener[mLogListeners.size()]);
		}
		for (int i = 0; i < listeners.length; i++) {
			final ILogListener listener = listeners[i];
			ISafeRunnable code = new ISafeRunnable() {
				public void run() throws Exception {
					listener.logging(status,
							DefaultLogger.this.mBundle.getSymbolicName());
				}

				public void handleException(Throwable e) {
					// Ignore
				}
			};
			SafeRunner.run(code);
		}
	}

	/**
	 * logt die Parameter
	 * 
	 * @param Serverity
	 * @param Code
	 * @param Message
	 * @param Throwable
	 */
	private void log(int sev, int code, String message, Throwable t) {
		log(createStatus(sev, code, message, t));
	}

	@Override
	public void removeLogListener(ILogListener listener) {
		mLog.removeLogListener(listener);
	}

	@Override
	public void info(String message, Throwable throwable) {
		log(IStatus.INFO, IStatus.OK, message, throwable);

	}

	@Override
	public void info(String message) {
		log(IStatus.INFO, IStatus.OK, message, null);

	}

	@Override
	public void error(String error) {
		log(IStatus.ERROR, IStatus.OK, error, null);
	}

	@Override
	public void error(Throwable t) {
		error(t.getMessage(), t);
	}

	@Override
	public void error(String s, Throwable t) {
		log(IStatus.ERROR, IStatus.OK, s, t);

	}

	/**
	 * Kreiert einen ExtendedStatus
	 * 
	 * @param sev
	 * @param code
	 * @param message
	 * @param exception
	 * @return
	 */
	private IStatus createStatus(int sev, int code, String message,
			Throwable exception) {
		String name = mBundle.getSymbolicName();
		return new DefaultExtendedStatus(sev, name, code,
				message != null ? message : "", exception);
	}

	@Override
	public ILog getLog() {
		return mLog;
	}

	@Override
	public void debug(String pMessage) {
		if (mFactory.isDebug()) {
			log(IExtendedStatus.DEBUG, IStatus.OK, pMessage, null);
		}

	}

}
