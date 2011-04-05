package ch.jester.orm;

import org.eclipse.osgi.util.NLS;

public class ORMMessages extends NLS {
	private static final String BUNDLE_NAME = "OSGI-INF.l10n.bundle"; //$NON-NLS-1$
	public static String DatabasePreferencePage_DatabaseLabel;
	public static String DatabasePreferencePage_Description;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, ORMMessages.class);
	}

	private ORMMessages() {
	}
}
