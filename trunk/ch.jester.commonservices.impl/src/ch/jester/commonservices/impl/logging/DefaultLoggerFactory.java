package ch.jester.commonservices.impl.logging;

import org.eclipse.core.runtime.Platform;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;

import ch.jester.common.logging.DefaultLogger;
import ch.jester.commonservices.api.ILogger;
import ch.jester.commonservices.api.ILoggerFactory;



public class DefaultLoggerFactory implements ILoggerFactory{
	private boolean mDebug = true;
	public DefaultLoggerFactory(){
		//System.out.println("DefaultLoggerFactory");
	}
	@Override
	public ILogger getLogger(Class<?> pClass) {
		Bundle bundle = FrameworkUtil.getBundle(pClass);
		DefaultLogger logger = new DefaultLogger(Platform.getLog(bundle), bundle, this);
		logger.addLogListener(new Log4jWrapper(pClass));
		return logger;
	}

	@Override
	public void setDebug(boolean b) {
		mDebug=b;
	}

	@Override
	public boolean isDebug() {
		return mDebug;
	}

	
}
