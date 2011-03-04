package ch.jester.common.tests.testservice;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import junit.framework.Assert;

import org.eclipse.core.runtime.ILogListener;
import org.eclipse.core.runtime.IStatus;

/**
 * Simpler Logger. (Es findet keine Ausgabe statt.)
 *
 */
public class LoggerMock implements ILogListener {
	public List<LogEntry> mLogs = new ArrayList<LogEntry>();
	List<LogEntry> mLogsExpected = new ArrayList<LogEntry>();
	private boolean mRecord;

	public LoggerMock(Class<?> c) {
	}

	@Override
	public void logging(IStatus status, String plugin) {
		String message = status.getMessage();
		Throwable exception = status.getException();
		if (mRecord) {
			this.mLogs.add(new LogEntry(status.getSeverity(), message,
					exception));
		}
	}

	static class LogEntry {
		int mStatus;
		String mMessage;
		Throwable mException;

		public LogEntry(int pLevel, String pMessage, Throwable pException) {
			mStatus = pLevel;
			mMessage = pMessage;
			mException = pException;
		}
	}

	public void record() {
		mRecord = true;
	}

	public void validate() {
		if (this.mLogsExpected.size() != this.mLogs.size()) {
			Assert.fail("List Size diff.");
		}
		Iterator<LogEntry> itLog = this.mLogs.iterator();
		for (LogEntry expected : this.mLogsExpected) {
			LogEntry actual = itLog.next();
			Assert.assertEquals("Message diff: ", actual.mMessage,
					expected.mMessage);
			Assert.assertEquals("Status diff: ", actual.mStatus,
					expected.mStatus);
			Assert.assertEquals("Exception diff: ", actual.mException,
					expected.mException);

		}
	}

	public void expected(int pLevel, String pMessage, Throwable pException) {
		mLogsExpected.add(new LogEntry(pLevel, pMessage, pException));

	}

}
