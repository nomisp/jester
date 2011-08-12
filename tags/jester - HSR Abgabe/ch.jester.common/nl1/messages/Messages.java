package messages;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = "messages.messages"; //$NON-NLS-1$
	public static String AbstractTableImporter_0;
	public static String AbstractTableImporter_cant_set_value1;
	public static String AbstractTableImporter_cant_set_value2;
	public static String AbstractTableImporter_headerlength_detaillength_dontmatch;
	public static String AbstractTableImporter_pls_check_settings;
	public static String AbstractTableImporter_processing_input;
	public static String AbstractTableImporter_reading_row;
	public static String AbstractTableImporter_save_db;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
