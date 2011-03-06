package ch.jester.commonservices.api.logging;

import org.eclipse.core.runtime.ILog;

public interface ILogger extends ILog {
public void info(String pMessage, Throwable pThrowable);
public void info(String pMessage);
public void debug(String pMessage);
public void error(String pError);
public void error(Throwable pThrowable);
public void error(String pMessage, Throwable pThrowable);
public ILog getLog();
}
