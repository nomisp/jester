package ch.jester.commonservices.impl.logging;



import org.apache.log4j.Logger;
import org.eclipse.core.runtime.ILogListener;
import org.eclipse.core.runtime.IStatus;

import ch.jester.common.logging.IExtendedStatus;


public class Log4jWrapper implements ILogListener {
	private Logger mLogger;
	public Log4jWrapper(Class<?> c){
		mLogger = Logger.getLogger(c);
	}
	@Override
	public void logging(IStatus status, String plugin) {
		String message = status.getMessage();
		Throwable exception = status.getException();
		switch (status.getSeverity()) {

		case IStatus.CANCEL:
			mLogger.error(message, exception);
			break;
		case IStatus.ERROR:
			mLogger.error(message, exception);
			break;
		case IStatus.INFO:
			mLogger.info(message, exception);
			break;
		case IExtendedStatus.DEBUG:
			mLogger.debug(message, exception);
			break;
		case IStatus.OK:
			mLogger.info(message, exception);
			break;
		case IStatus.WARNING:
			mLogger.warn(message, exception);
			break;
		default:
			mLogger.info(message, exception);
			break;
		}

	}

}
