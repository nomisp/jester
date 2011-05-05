package ch.jester.orm.internal;

import org.eclipse.osgi.util.NLS;

public class ORMMessages extends NLS {
	private static final String BUNDLE_NAME = "OSGI-INF.l10n.bundle"; //$NON-NLS-1$
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, ORMMessages.class);
	}

	private ORMMessages() {
	}

	public static String DatabasePreferencePage_DatabaseLabel;
	public static String DatabasePreferencePage_Description;
	public static String DatabasePreferencePage_DatabaseManager;
	public static String DatabasePreferencePage_DatabaseName;
	public static String DatabasePreferencePage_JDBCDriver;
	public static String DatabasePreferencePage_ORMConfiguration;
	public static String DatabasePreferencePage_SQLDialect;
	public static String DatabasePreferencePage_Subprotocol;
}
