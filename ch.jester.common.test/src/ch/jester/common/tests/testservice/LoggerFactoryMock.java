package ch.jester.common.tests.testservice;

import org.eclipse.core.runtime.Platform;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;

import ch.jester.common.logging.DefaultLogger;
import ch.jester.commonservices.api.ILogger;
import ch.jester.commonservices.api.ILoggerFactory;

public class LoggerFactoryMock implements ILoggerFactory {
	private boolean mDebug = true;
	private LoggerMock mLogger;

	@Override
	public ILogger getLogger(Class<?> pClass) {
		Bundle bundle = FrameworkUtil.getBundle(pClass);
		DefaultLogger logger = new DefaultLogger(Platform.getLog(bundle),
				bundle, this);
		logger.addLogListener(mLogger = new LoggerMock(pClass));
		return logger;
	}

	public LoggerMock getLogger() {
		return mLogger;
	}

	@Override
	public void setDebug(boolean b) {
		mDebug = b;
	}

	@Override
	public boolean isDebug() {
		return mDebug;
	}

}
