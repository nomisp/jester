package ch.jester.common.logging;

import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.Status;

/**
 * Klasse Status wird mit Debug Status erweitert
 * 
 * @see org.eclipse.core.runtime.Status
 * 
 */
public class DefaultExtendedStatus extends Status implements IExtendedStatus {

	private int severity;

	public DefaultExtendedStatus(int severity, String pluginId, String message,
			Throwable exception) {
		super(severity, pluginId, message, exception);
	}

	public DefaultExtendedStatus(int severity, String pluginId, String message) {
		super(severity, pluginId, message);
	}

	public DefaultExtendedStatus(int severity, String pluginId, int code,
			String message, Throwable exception) {
		super(severity, pluginId, code, message, exception);
	}

	@Override
	protected void setSeverity(int severity) {
		Assert.isLegal(severity == DEBUG || severity == OK || severity == ERROR
				|| severity == WARNING || severity == INFO
				|| severity == CANCEL);
		if (severity != DEBUG) {
			super.setSeverity(severity);
		}
		this.severity = severity;
	}

	@Override
	public int getSeverity() {
		return this.severity;
	}
}
