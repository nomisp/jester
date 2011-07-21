package messages;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = "messages.messages"; //$NON-NLS-1$
	public static String AbstractPlayerImporter_estimatedelo;
	public static String AbstractPlayerImporter_club;
	public static String AbstractPlayerImporter_age;
	public static String AbstractPlayerImporter_checkingduplicates;
	public static String AbstractPlayerImporter_city;
	public static String AbstractPlayerImporter_elo;
	public static String AbstractPlayerImporter_fidecode;
	public static String AbstractPlayerImporter_firstname;
	public static String AbstractPlayerImporter_lastname;
	public static String AbstractPlayerImporter_nation;
	public static String AbstractPlayerImporter_nationalcode;
	public static String AbstractPlayerImporter_nationalelo;
	public static String AbstractPlayerImporter_savingplayers;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
